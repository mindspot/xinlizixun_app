package common.repository.http;


import android.content.Context;

import com.framework.http.UserAgent;
import com.paopao.paopaostudio.BuildConfig;

import common.repository.http.api.App;
import common.repository.http.fun.HttpEventController;
import common.repository.http.param.RequestHeaderHelper;

/**
 * Created by Administrator on 2017/3/6.
 */
public class HttpApi {


    public static void init(Context context, String channelName) {
        UserAgent.newInstance(BuildConfig.USER_AGENT, BuildConfig.VERSION_NAME);
        RequestHeaderHelper.init(context, channelName);
        HttpApiBase.init(context, !BuildConfig.IS_RELLEASE);
        new HttpEventController(context);
    }

    public static void cancelRequest(Object tag) {
        HttpApiBase.cancelRequest(tag);
    }

    public static String getUrl(String baseUrl) {
        return HttpApiBase.getHttp().getUrl(baseUrl);
    }

    public static App app() {
        return App.instance();
    }
}
