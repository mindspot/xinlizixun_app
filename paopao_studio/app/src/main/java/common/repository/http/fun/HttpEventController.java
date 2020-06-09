package common.repository.http.fun;

import android.content.Context;


import com.framework.page.Page;

import common.events.HttpEvent;
import de.greenrobot.event.EventBus;
import util.CommonPopupWindowUtil;

/**
 * Created by Administrator on 2017/12/14.
 * http中共通的响应处理
 * 全局的，不需要注销监听
 */
public class HttpEventController {

    private final Context context;
    private final ReloginFun reloginFun;

    public HttpEventController(Context context) {
        this.context = context;
        EventBus.getDefault().register(this);
        reloginFun = new ReloginFun();
    }


    public void onEventMainThread(HttpEvent event) {
        switch (event.getCode()) {
            case HttpEvent.HTTP_ERROR_NEED_LOGIN:
                if (reloginFun != null) {
                    reloginFun.execute();
                }
                break;
            case HttpEvent.HTTP_ERROR_REQUEST_CHECK_VERIFICATION_CODE:
                checkVerificationCode();
                break;
            default:
                break;
        }
    }

    private void checkVerificationCode() {
        if (CommonPopupWindowUtil.activities == null || CommonPopupWindowUtil.activities.size() <= 0) {
            return;
        }
        Page.ActivityPage activity = CommonPopupWindowUtil.activities.get(CommonPopupWindowUtil.activities.size() - 1);

    }

}
