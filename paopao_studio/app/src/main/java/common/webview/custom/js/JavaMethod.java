package common.webview.custom.js;

import android.webkit.JavascriptInterface;

import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.ViewUtil;
import com.socks.library.KLog;

import common.repository.http.param.RequestHeaderHelper;
import common.repository.share_preference.SPApi;
import common.router.CommandEntity;
import common.router.CommandRouter;
import common.webview.custom.BaiTiaoWebView;

/**
 * webview中的绑定对象
 *
 * @author Administrator
 */
public class JavaMethod {

    private final BaiTiaoWebView webView;
    /**
     * H5 页面显示在前台 回调函数
     */
    private String onShowCallback;

    public JavaMethod(BaiTiaoWebView webView) {
        this.webView = webView;
    }

    private void postMethod(final String method) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(method);
            }
        });
    }

    public String getOnShowCallback() {
        return onShowCallback;
    }

    /**
     * js 调用该方法时 判断是否显示 第一次认证流程中是否显示下一步按钮
     *
     * @return isFirstCertificationNext 0，不显示，1显示
     */
    @JavascriptInterface
    public int isFirstCertificationNext() {
//            认证中心流程 1 原流程 2新流程
        int ccVersion = SPApi.config().getCcVersion();
        if (ccVersion == 1) {
            //原流程中H5不显示下一步
            return 0;
        } else {
            //已实名认证，不显示下一步，未实名认证，显示下一步
            return SPApi.app().getFisrtCCProcess() == 0 ? 1 : 0;
        }
    }

    @JavascriptInterface
    public void returnNativeMethod(String typeStr) {
        KLog.w("js-param:" + typeStr);
        final CommandEntity bean = CommandRouter.convert(typeStr);
        webView.post(new Runnable() {
            @Override
            public void run() {
                bean.request().setPage(webView.getPage()).router();
            }
        });
    }

    /**
     * H5 页面显示在前台
     */
    @JavascriptInterface
    public void onShow(String callback) {
        onShowCallback = callback;
    }


    /**
     * 清除webview 历史
     */
    private void clearHistory() {
        if (webView != null) {
            webView.clearHistory();
            webView.clearCache(true);
        }
    }

    /**
     * 获取header数据
     *
     * @return header的Json格式数据
     */
    @JavascriptInterface
    public String getHeaders() {
        return ConvertUtil.toJsonString(RequestHeaderHelper.getHeaders());
    }

    @JavascriptInterface
    public String getDeviceId() {
        return ViewUtil.getDeviceId(webView.getPage().context());
    }

    /**
     * 判断支付宝客户端是否安装
     * @return
     */
    @JavascriptInterface
    public boolean checkAliPayInstalled() {
        return ViewUtil.isAliPayInstalled(webView.getPage().context());
    }
}