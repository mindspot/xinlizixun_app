package common.webview.custom.util;

import android.view.View;

import com.framework.page.Page;

import base.UserCenter;

/**
 * webview的工具类
 *
 * @author Administrator
 * @date 2017/12/27
 */
public class WebviewUtils {


    private WebviewUtils() {
    }

    private static final String UMENG = "umeng";


    /**
     * 登录请求
     *
     * @return 是否为登录请求的URL
     */
    public static boolean handleLogin(Page page, View view, String url) {
        boolean isLoginUrl = url.contains("LOGIN://USER/LOGIN");
        if (isLoginUrl) {
            UserCenter.instance().toLogin(page);
        }
        return isLoginUrl;
    }
}
