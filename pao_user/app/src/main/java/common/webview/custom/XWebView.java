package common.webview.custom;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.framework.http.UserAgent;
import com.framework.utils.LogUtil;
import com.framework.utils.StringUtil;
import com.socks.library.KLog;

import common.repository.http.param.RequestHeaderHelper;
import common.webview.custom.js.JavaMethod;
import common.webview.custom.util.WebviewUtils;
import util.AndroidBug5497Workaround;

/**
 * 封装的WebView
 *
 * @author Administrator
 * @date 2017/9/12
 */
public class XWebView extends WebView implements BaiTiaoWebView {

    private WebViewPage page;
    private WebViewConfig config;
    private UIHandler uiHandler;

    private JavaMethod nativeMethodJsObj;

    private WangYiServiceCall wangYiServiceCall;

    public XWebView(Context context) {
        super(context, null);
    }

    public XWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    //************* config *************

    @Override
    public void initConfig(@NonNull WebViewPage page, @NonNull WebViewConfig config, @Nullable UIHandler uiHandler) {
        this.page = page;
        this.config = config;
        this.uiHandler = uiHandler;
        configBuild();
        //从首页webview 进入
        //解决H5 输入框 被软键盘遮挡问题
        AndroidBug5497Workaround.assistActivity(page.activity());
        wangYiServiceCall = new WangYiServiceCall(page);
    }

    private void configBuild() {
        LogUtil.Log("XWebView", "url=" + config.getUrl());
        initTitle();
        if (nativeMethodJsObj == null) {
            nativeMethodJsObj = new JavaMethod(this);
        }

        initSetting();
        initLisenter();
        addJavascriptInterface(config.getUrl());
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

    private void initLisenter() {
        page.getToolBarTitleView().setLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                page.activity().onBackPressed();// 等同于点击返回键，会调用onBackPressed4Activity方法
            }
        });
        page.getToolBarTitleView().setCloseImgListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                close4Activity();
            }
        });
        this.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (StringUtil.isBlank(url)) {
                    return;
                }

            }
        });
    }

    private void addJavascriptInterface(String url) {
        addJavascriptInterface(nativeMethodJsObj, NATIVE_METHOD);
    }

    private void setWebViewClient() {
        setWebViewClient(new WebViewClient() {
            //在API 24以后过时
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                KLog.d("url:" + url);

                if (WebviewUtils.handleLogin(page, view, url)) {
                    return true;
                }
                addJavascriptInterface(url);
                view.loadUrl(url, RequestHeaderHelper.getHeaders());
                return true;
            }

            //在API 24以后新加
            @Override
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                KLog.d("request:" + request + ", method:" + request.getMethod());
                Uri uri = request.getUrl();
                KLog.d("uri:" + uri);
                String url = uri.toString();

                if (WebviewUtils.handleLogin(page, view, url)) {
                    return true;
                }

                return false;
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
                page.getToolBarTitleView().getRight_btn_tv().setVisibility(View.GONE);

                if (uiHandler != null) {
                    uiHandler.onPageStarted();
                }
            }

            @Override
            public void onPageFinished(final WebView view, String url) {
                KLog.d("url:" + url);
                super.onPageFinished(view, url);
                view.loadUrl("javascript:setWebViewFlag()");
                page.getProgressBarHorizontal().setAnimProgress2(100);
                if (view.canGoBack()) {
                    page.getToolBarTitleView().showCloseImg();
                } else {
                    page.getToolBarTitleView().hideCloseImg();
                }
                if (!StringUtil.isBlank(view.getTitle())) {
                    refreshTitle(view.getTitle());
                }
                view.loadUrl("javascript:window." + config.getAuthMethod() + ".showSource('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

                if (uiHandler != null) {
                    uiHandler.onPageFinished(XWebView.this, url);
                }
            }


        });
    }

    private void setWebChromeClient() {
        setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                KLog.d("newProgress:" + newProgress);
                page.getProgressBarHorizontal().setAnimProgress2(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                KLog.d("title:" + title);
                super.onReceivedTitle(view, title);
                refreshTitle(title);
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                wangYiServiceCall.openFileChooser(uploadMsg);
            }

            // For Android >=3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                wangYiServiceCall.openFileChooser(uploadMsg, acceptType);
            }

            // For Android  >= 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

            // For Android  >= 5.0
            @Override
            @SuppressLint("NewApi")
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null
                        && fileChooserParams.getAcceptTypes().length > 0 && fileChooserParams.getAcceptTypes()[0].indexOf("image/") != -1) {
                    wangYiServiceCall.onShowFileChooser(filePathCallback);
                } else {
                    wangYiServiceCall.onReceiveValue();
                }
                return true;
            }
        });
    }


    //************* 设置标题 *************

    private void initTitle() {
        page.getToolBarTitleView().setTitle("");
    }

    private void refreshTitle(String title) {
        page.getToolBarTitleView().setTitle("");
    }


    @Override
    public WebViewPage getPage() {
        return page;
    }

    @Override
    public void loadUrl() {
        loadUrl(config.getUrl(), RequestHeaderHelper.getHeaders());
    }


    //************* 回退、关闭页面功能 *************

    private void close4Activity() {
        goBack4Activity();
    }

    /**
     * activity的返回键拦截
     * 只针对XWebView在activity中的逻辑，现在还没有在fragment中的XWebView
     */
    @Override
    public void onBackPressed4Activity() {
        if (canGoBack()) {
            goBack();
        } else {
            goBack4Activity();
        }
    }


    /**
     * 只针对XWebView在activity中的逻辑，现在还没有在fragment中的XWebView，命名区别与WebView的goBack方法，
     */
    @Override
    public void goBack4Activity() {
        if (config.isPush()) {
//            Intent intent = new Intent(page.activity(), SplashActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            page.startActivity(intent);
            page.activity().finish();
        } else {
            if (JUMP_TO_HOME_1.equals(config.getJumpToHome())) {
//                Intent intent = new Intent(page.activity(), StoreMainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                page.startActivity(intent);
                page.activity().finish();
            } else {
                page.activity().finish();
            }
        }
    }

    public void onShow() {
        if (nativeMethodJsObj != null && !TextUtils.isEmpty(nativeMethodJsObj.getOnShowCallback())) {
            this.loadUrl("javascript:" + nativeMethodJsObj.getOnShowCallback() + "()");
        }
    }

    @Override
    public void destroy() {
        clearHistory();
        super.destroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        wangYiServiceCall.onActivityResult(requestCode, resultCode, data);
    }
}
