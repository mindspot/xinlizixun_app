package module.update;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.view.View.OnClickListener;

import com.framework.BuildConfig;
import com.framework.component.ui.dailog.AlertDialog;
import com.framework.page.Page;
import com.framework.utils.ViewUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UpdateView {

    private static final int START = 1;
    private static final int PROGRESS = 2;
    private static final int END = 3;
    private static final int ERROR = 4;

    private final Page page;
    private final UpdateListener updateListener;

    private ProgressDialog pd = null;

    private int totalSize;
    private int count = 0;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (ViewUtil.isFinishedPage(page)) {
                return;
            }
            switch (msg.what) {
                case START:
                    if (pd == null || !pd.isShowing()) {
                        pd = new ProgressDialog(page.context());
                        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pd.setCancelable(false);
                        pd.setMessage("正在下载更新");
                        pd.setMax(totalSize);
                        pd.show();
                    }
                    break;
                case PROGRESS:
                    pd.setProgress(count);
                    break;
                case END:
                    dismiss();
                    if (updateListener != null) {
                        updateListener.onUpdateComplete();
                    }
                    break;
                case ERROR:
                    dismiss();
                    if (updateListener != null) {
                        updateListener.onUpdateFailed();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public UpdateView(Page page, UpdateListener updateListener) {
        this.page = page;
        this.updateListener = updateListener;
    }

    private void dismiss() {
        if (pd == null) {
            return;
        }

        if (!pd.isShowing()) {
            pd = null;
            return;
        }

        if (ViewUtil.isFinishedPage(page)) {
            pd = null;
            return;
        }

        pd.dismiss();
        pd = null;
    }


    public void downloadNewVersionApk(final String downloadUrl, final String fileName) {
        new Thread(new DownloadRunnable(downloadUrl, fileName)).start();
    }

    private void installApk(File file) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//判断版本大于等于7.0
                data = FileProvider.getUriForFile(page.context(), page.context().getPackageName() + ".fileprovider", file);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);// 给目标应用一个临时授权
                intent.setDataAndType(data, "application/vnd.android.package-archive");
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//安装完成后提示：完成 打开
            page.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            new AlertDialog(page.context()).builder().setCancelable(false)
                    .setTitle("无法自动安装更新包")
                    .setMsg("请打开存储设备中" + file.getAbsolutePath() + "/latest.apk文件安装")
                    .setNegativeButton("我知道了", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
        }
    }

    public interface UpdateListener {
        void onUpdateComplete();

        void onUpdateFailed();
    }

    private class DownloadRunnable implements Runnable {
        private final String downloadUrl;
        private final String fileName;

        private DownloadRunnable(String downloadUrl, String fileName) {
            this.downloadUrl = downloadUrl;
            this.fileName = fileName;
        }

        @Override
        public void run() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                try {
                    URL url = new URL(downloadUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    boolean useHttps = downloadUrl.startsWith("https");
                    if (useHttps) {
                        HttpsURLConnection https = (HttpsURLConnection) conn;
                        trustAllHosts(https);
                        https.setHostnameVerifier(DO_NOT_VERIFY);
                    }
                    conn.setConnectTimeout(20000);
                    conn.setReadTimeout(20000);
                    totalSize = conn.getContentLength();
                    handler.sendEmptyMessage(START);
                    InputStream is = conn.getInputStream();
                    FileOutputStream fileOutputStream = null;
                    File file = null;
                    if (is != null) {
                        File dir = new File(page.context().getExternalFilesDir(null).toString() + "/download/");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        file = new File(dir, fileName);
                        if (file.exists()) {
                            file.delete();
                        }
                        fileOutputStream = new FileOutputStream(file);
                        byte[] buf = new byte[4096];
                        int temp = -1;
                        while ((temp = is.read(buf)) != -1) {
                            fileOutputStream.write(buf, 0, temp);
                            count += temp;
                            handler.sendEmptyMessage(PROGRESS);
                        }
                    }
                    fileOutputStream.flush();
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    handler.sendEmptyMessage(END);
                    if (file.exists()) {
                        installApk(file);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(ERROR);
                } catch (Exception e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(ERROR);
                }
            }
        }
    }

    /**
     * 覆盖java默认的证书验证
     */
    private static final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }
    }};

    /**
     * 设置不验证主机
     */
    private static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * 信任所有
     *
     * @param connection
     * @return
     */
    private static SSLSocketFactory trustAllHosts(HttpsURLConnection connection) {
        SSLSocketFactory oldFactory = connection.getSSLSocketFactory();
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();
            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oldFactory;
    }
}
