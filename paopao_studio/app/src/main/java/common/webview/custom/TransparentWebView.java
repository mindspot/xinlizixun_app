package common.webview.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.framework.http.UserAgent;
import com.socks.library.KLog;

/**
 * 透明的WebView
 *
 * @author Administrator
 * @date 2017/9/12
 */
@SuppressLint("ViewConstructor")
public class TransparentWebView extends WebView {

    private final UIHandler uiHandler;

    public TransparentWebView(Context context, @NonNull UIHandler uiHandler) {
        super(context, null);
        this.uiHandler = uiHandler;
        init();
    }


    private void init() {
        setVisibility(View.INVISIBLE);
        initSetting();
        setWebViewClient();
        setWebChromeClient();
    }

    private void initSetting() {
        setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        setSettings();//WebView属性设置！！！
    }

    private void setSettings() {
        WebSettings settings = getSettings();
        settings.setTextZoom(100);
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        //开启DOM形式存储
        settings.setDomStorageEnabled(true);
        //开启数据库形式存储
        settings.setDatabaseEnabled(true);
//        //缓存数据的存储地址
//        String appCacheDir = page.context().getDir("cache", Context.MODE_PRIVATE).getPath();
//        settings.setAppCachePath(appCacheDir);
//        //开启缓存功能
//        settings.setAppCacheEnabled(true);
        //缓存模式
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAllowFileAccess(true);

        settings.setUserAgentString(settings.getUserAgentString() + UserAgent.get());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
    }

    private void setWebViewClient() {
        setWebViewClient(new WebViewClient() {
            //在API 24以后过时
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                KLog.d("url:" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }

            //在API 24以后新加
            @Override
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Uri uri = request.getUrl();
                KLog.d("request:" + request + ", method:" + request.getMethod() + ", url:" + uri.toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                KLog.d("error:" + error);
                handler.proceed();  //接受所有证书
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                KLog.d("url:" + url);
                super.onPageStarted(view, url, favicon);
                uiHandler.onPageStarted();
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                KLog.d("url:" + url);
                super.onPageFinished(view, url);
                uiHandler.onPageFinished(TransparentWebView.this, url);
            }
        });
    }

    private void setWebChromeClient() {
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                KLog.d("newProgress:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }


    @Override
    public void destroy() {
        clearHistory();
        super.destroy();
    }


    public interface UIHandler {
        void onPageStarted();

        void onPageFinished(TransparentWebView view, String url);
    }


}
