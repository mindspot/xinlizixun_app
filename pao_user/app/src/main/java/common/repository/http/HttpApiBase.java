package common.repository.http;

import android.content.Context;

import util.ServiceConfig;

/**
 * Created by Administrator on 2017/3/6.
 */
public class HttpApiBase {

    private static BaseHttp baseHttp;

    static void init(final Context context, boolean printLog) {
        baseHttp = new BaseHttp(context, printLog);
    }

    public static BaseHttp getHttp() {
        return baseHttp;
    }

    static void cancelRequest(Object tag) {
        baseHttp.cancelRequest(tag);
    }

    public String getServiceUrl(String url) {
        return baseHttp.getUrl(ServiceConfig.SERVICE_BASE_URL + url);
    }
}
