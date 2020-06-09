package module.app;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.framework.http.UserAgent;
import com.framework.utils.LogUtil;
import com.framework.utils.ViewUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.push.EMPushConfig;
import com.lib.image.loader.glide.GlideImageLoader;
import com.paopao.paopaostudio.BuildConfig;
import com.paopao.paopaostudio.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;

import common.repository.http.HttpApi;
import common.repository.http.param.BaseRequestBean;
import common.repository.share_preference.SPApi;
import im.CallReceiver;
import im.HMSPushHelper;
import im.MyUserProvider;
import util.PreferenceManager;
import util.Tool;
import util.location.DLocationUtils;


public class MyApplication extends DataApplication {
    private static final String TAG = "MyApplication";

    public static MyApplication app;

    private String filesPath;
    public String imgPath;
    private String channelName;// 应用的渠道名称

    private CallReceiver callReceiver;
    private boolean isCallReceiver = false;

    static {
        //定义全局刷新头部
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsHeader(context);
            }
        });
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @NonNull
            @Override
            public RefreshFooter createRefreshFooter(@NonNull Context context, @NonNull RefreshLayout layout) {
                return new ClassicsFooter(context);
            }
        });

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        Log.d("Administrator", "application onTerminate");
        HttpApi.cancelRequest(this);
        EaseUI.getInstance().onTerminate();
        if (isCallReceiver) {
            unregisterReceiver(callReceiver);
            isCallReceiver = false;
        }
        // 注销
        DLocationUtils.getInstance().unregister();
        super.onTerminate();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        String currProcessName = ViewUtil.getCurProcessName(this);
        if (BuildConfig.APPLICATION_ID.equals(currProcessName)) {// main process: APP的主进程
            KLog.d("main process onCreate");
            handleMainProcess();
        } else if ((BuildConfig.APPLICATION_ID + ":pushcore").equals(currProcessName)) {// pushcore process: 极光推送的进程
            KLog.d("jpush pushcore process onCreate");
        } else {
            KLog.e("unknown process onCreate");
        }
        //开启Webview 调试
        if (!BuildConfig.IS_RELLEASE &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
    }

    private void handleMainProcess() {
        MyApplication.app = this;
        initCrashReport();
        getChannel();
        SPApi.init(this);

        HttpApi.init(getApplicationContext(), channelName);
        configImageLoader();
        initFilePath();
        initLoadingView(getApplicationContext());

        LogUtil.isDebug = true;
        //记录visit_id的时间戳
        long visitIdTime = System.currentTimeMillis();
        SPApi.app().setVisitIdTimeStamp(String.valueOf(visitIdTime));

        initUmeng();
        initEMClient();

        // 定位初始化
        DLocationUtils.init(this);
        getLocation();
    }

    private void initCrashReport() {
        // 为了提高合作方的webview场景稳定性，及时发现并解决x5相关问题，当客户端发生crash等异常情况并上报给服务器时请务必带上x5内核相关信息。
        // x5内核异常信息获取接口为：com.tencent.smtt.sdk.WebView.getCrashExtraMessage(context)。以bugly日志上报为例：
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(getApplicationContext());
        CrashReport.initCrashReport(getApplicationContext(), BuildConfig.BUILD_TYPE, true, strategy);
    }

    private static final String MAIN_CHANNEL = "paopaostudio";

    private void getChannel() {
        if (BuildConfig.DEBUG) {
            channelName = MAIN_CHANNEL + "_test";
            return;
        }
        String packerNgChannelName = Tool.getAppMetaData(this, "MTA_CHANNEL");
        if (packerNgChannelName == null || packerNgChannelName.trim().isEmpty()) {
            channelName = MAIN_CHANNEL;
            CrashReport.postCatchedException(new Throwable("未找到打包的渠道"));
        } else {
            channelName = MAIN_CHANNEL + "_" + packerNgChannelName;
        }
        Log.w(TAG, "channelName:" + channelName);
    }


    /**
     * 获取系统的当前时间时间
     */
    public static String getAppCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new java.util.Date());
        return date;
    }

    private void configImageLoader() {
        // add common header(cookie)
        Map<String, String> headers = new BaseRequestBean().getHeaders();
        headers.put(UserAgent.KEY, UserAgent.get());
        GlideImageLoader.init(headers);
    }


    private void initFilePath() {
        // 初始化环境变量
        this.filesPath = this.getFilesDir().getAbsolutePath();
        this.imgPath = this.filesPath + "/img/";
        File f = new File(imgPath);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    public static void initLoadingView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loan_loading, null);
        ViewUtil.setLoadingView(context, view);
    }

    public static void loadingDefault(Activity context) {
        ViewUtil.setLoadingView(context, null);
        ViewUtil.showLoading(context, "");
    }

    public static void loadingContent(Activity context, String content) {
        ViewUtil.setLoadingView(context, null);
        ViewUtil.showLoading(context, content);
    }

    public static void hideLoading() {
        ViewUtil.hideLoading();
    }

    public String getChannelName() {
        return channelName;
    }

    public void initUmeng() {
        PlatformConfig.setWeixin("", "");
        PlatformConfig.setSinaWeibo("", "", "");
        PlatformConfig.setQQZone("", "");
        /**
         * 设置日志加密
         * 参数：boolean 默认为false（不加密）
         */
        UMConfigure.setEncryptEnabled(true);
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
        /**
         * 初始化common库
         * 参数1:上下文，不能为空
         * 参数2:【友盟+】 AppKey
         * 参数3:【友盟+】 Channel
         * 参数4:设备类型，UMConfigure.DEVICE_TYPE_PHONE为手机、UMConfigure.DEVICE_TYPE_BOX为盒子，默认为手机
         * 参数5:Push推送业务的secret
         */
        UMConfigure.init(this, "", channelName, UMConfigure.DEVICE_TYPE_PHONE, "");
    }

    //初始化环信
    public void initEMClient() {
        EMPushConfig.Builder builder = new EMPushConfig.Builder(app);
        builder.enableMeiZuPush("1009424", "4c51a96d69284fa383be4ccee84ee497")
                .enableMiPush("2882303761518343192", "5521834356192")
                .enableOppoPush("c4628ed83b5642af85ca4b14257fbb63", "c98dc469762948ffab042d9bf29ac871")
                //builder.enableVivoPush() // 推送证书相关信息配置在AndroidManifest.xml中
                .enableHWPush(); //开发者需要调用该方法来开启华为推送

        EMOptions options = new EMOptions();
        //推送配置
        options.setPushConfig(builder.build());
        //是否开启自动登录
        options.setAutoLogin(true);
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);
        //EaseUI初始化
        if (EaseUI.getInstance().init(app.getApplicationContext(), options)) {
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            // 初始化华为 HMS 推送服务, 需要在SDK初始化后执行
            HMSPushHelper.getInstance().initHMSAgent(app);
            //设置用户昵称头像的显示
            EaseUI.getInstance().setUserProfileProvider(MyUserProvider.getInstance());
            initCallReceiver();
        }
    }

    public void initCallReceiver() {
        PreferenceManager.init(app);
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }
        //register incoming call receiver
        registerReceiver(callReceiver, callFilter);
        isCallReceiver = true;
    }

}
