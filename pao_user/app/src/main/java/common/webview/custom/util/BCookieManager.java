package common.webview.custom.util;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import java.util.LinkedHashMap;
import java.util.Map;

import common.repository.http.entity.user.UserInfoBean;

public class BCookieManager {

    public static void setCookie(UserInfoBean userInfo, Context context) {
        clearCookie(context);
        setCookieForAndroid(userInfo, context);
    }

    private static void setCookieForAndroid(UserInfoBean userInfo, Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        LinkedHashMap<String, String> cookies = createCookies(userInfo);
        for (Map.Entry<String, String> cookieEntry : cookies.entrySet()) {
            cm.setCookie(cookieEntry.getKey(), cookieEntry.getValue());
        }
        CookieSyncManager.getInstance().sync();
    }

    private static LinkedHashMap<String, String> createCookies(UserInfoBean userInfo) {
        LinkedHashMap<String, String> cookies = new LinkedHashMap<>(16);
        final String cookie = "authorization=Bearer " + userInfo.getToken();
        cookies.put("*.zicp.vip", cookie);
        return cookies;
    }


    public static void clearCookie(Context context) {
        clearCookieForAndroid(context);
    }

    private static void clearCookieForAndroid(Context context) {
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }
}
