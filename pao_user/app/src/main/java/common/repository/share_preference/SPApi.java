package common.repository.share_preference;

import android.app.Application;

import common.repository.share_preference.api.SPApp;
import common.repository.share_preference.api.SPConfig;
import common.repository.share_preference.api.SPUser;


/**
 * Created by Administrator on 2017/3/13.
 * share preferences的API公开调用类
 */
public class SPApi {

    private static Application mApp;

    public static void init(Application app) {
        mApp = app;
        SPUser.init(app);
    }


    private static SPApp app;
    private static SPConfig config;


    public static SPApp app() {
        if (app == null) {
            app = new SPApp(mApp);
        }
        return app;
    }

    public static SPConfig config() {
        if (config == null) {
            config = new SPConfig(mApp);
        }
        return config;
    }

    public static SPUser user() {
        return SPUser.instance();
    }


    public static void clear() {
        user().clear();
    }

}
