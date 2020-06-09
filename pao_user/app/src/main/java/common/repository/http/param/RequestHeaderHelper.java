package common.repository.http.param;

import android.content.Context;

import com.framework.utils.LogUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.ViewUtil;
import com.paopao.paopaouser.BuildConfig;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import common.repository.share_preference.api.SPUser;
import okhttp3.Headers;

/**
 * Created by Administrator on 2017/6/15.
 */

public class RequestHeaderHelper {

    private static Context mContext;
    private static Map<String, String> headers = Collections.EMPTY_MAP;


    public static void init(Context context, String appMarket) {
        mContext = context;
        headers = interpret(createHeaders(appMarket));
    }

    private static LinkedHashMap<String, String> createHeaders(String appMarket) {
        LinkedHashMap<String, String> headers = new LinkedHashMap<>(6);
        headers.put("clientType", "android");
        headers.put("appVersion", BuildConfig.VERSION_NAME);
        headers.put("deviceId", ViewUtil.getDeviceId(mContext));
        headers.put("deviceName", ViewUtil.getDeviceName());
        headers.put("osVersion", ViewUtil.getOsVersion());
        headers.put("appMarket", appMarket);
        headers.put("bundleId", BuildConfig.APPLICATION_ID);
        return headers;
    }

    private static Map<String, String> interpret(Map<String, String> headers) {
        Map<String, String> newHeaders = new LinkedHashMap<>();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            try {
                new Headers.Builder().add("check", header.getValue());
                newHeaders.put(header.getKey(), header.getValue());
            } catch (IllegalArgumentException | NullPointerException e) {
                KLog.w("http header key is error...", headers, e);
                CrashReport.postCatchedException(new Throwable("common request header error:" + SPUser.instance().getUID()
                        + "\n--> key:" + header.getKey() + ", value:" + header.getValue()
                        + "\n--> headers:" + headers.toString(), e));
            }
        }
        return newHeaders;
    }


    public static Map<String, String> getHeaders() {
        return headers;
    }

    public static Map<String, String> getCookies() {
        String sessionid = SPUser.instance().getSessionId();
        return getCookies(sessionid);
    }

    public static Map<String, String> getCookies(String cookies) {
        String sessionid = SPUser.instance().getSessionId();
        if (StringUtil.isBlank(sessionid)) {
            LogUtil.Log("header Cookies", "Cookies: " + sessionid);
            return Collections.EMPTY_MAP;
        }
        Map<String, String> header = new HashMap<>(1);
        header.put("authorization", "Bearer " + sessionid);
        return header;
    }
}
