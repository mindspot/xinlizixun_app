package common.webview.custom;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framework.page.Page;

import ui.bar.WebViewProgressBar;
import ui.title.ToolBarTitleView;

/**
 * 封装的WebView
 *
 * @author Administrator
 * @date 2017/12/27
 */
public interface BaiTiaoWebView {

    String NATIVE_METHOD = "nativeMethod";

    String EXTRA_TITLE = "title";
    String EXTRA_URL = "url";
    String EXTRA_AUTH_METHOD = "authMethod";
    String EXTRA_IS_PUSH = "isPush";
    String EXTRA_JUMP_TO_HOME = "JumpToHome";
    String JUMP_TO_HOME_1 = "1";
    String EXTRA_IS_SHARE = "isShare";


    //************* config *************

    void initConfig(@NonNull WebViewPage page, @NonNull WebViewConfig config, @Nullable UIHandler uiHandler);

//    boolean onActivityResult(int requestCode, int resultCode, Intent data);


    //************* 设置标题 *************

    /**
     * webview所在页面的接口
     */
    interface WebViewPage extends Page {

        ToolBarTitleView getToolBarTitleView();

        LinearLayout getDialogView();

        TextView getTvTagContent();

        WebViewProgressBar getProgressBarHorizontal();

        BaiTiaoWebView getWebView();
    }

    WebViewPage getPage();

    void loadUrl();


    //************* 回退、关闭页面功能 *************

    /**
     * activity的返回键拦截
     * 只针对XWebView在activity中的逻辑，现在还没有在fragment中的XWebView
     */
    void onBackPressed4Activity();


    /**
     * 只针对XWebView在activity中的逻辑，现在还没有在fragment中的XWebView，命名区别与WebView的goBack方法，
     */
    void goBack4Activity();


    //************* webview的ui更改处理回调 *************

    /**
     * webview的ui更改处理回调：用于不同页面中，处理特殊的UI显示
     * tip: 只处理UI和listener
     * 都是在方法的最后面调用(覆盖掉前面的UI效果)
     */
    interface UIHandler {
        void onPageStarted();

        void onPageFinished(BaiTiaoWebView view, String url);
    }


    //************* view & webview *************

    boolean post(Runnable action);

    boolean postDelayed(Runnable action, long delayMillis);

    boolean canGoBack();

    void goBack();

    void loadUrl(String url);

    void setVisibility(int visibility);

    void clearHistory();

    void clearCache(boolean includeDiskFiles);

    void reload();

    void destroy();

    void onActivityResult(int requestCode, int resultCode, Intent data);
}
