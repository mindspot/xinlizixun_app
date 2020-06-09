package common.repository.http.fun;

import android.content.DialogInterface;
import android.view.View;

import com.framework.component.ui.dailog.AlertDialog;
import com.framework.page.Page;
import com.paopao.paopaouser.R;

import base.UserCenter;
import util.CommonPopupWindowUtil;

/**
 * Created by Administrator on 2017/5/26.
 * 重新登录
 */
public class ReloginFun {

    private AlertDialog alertDialog;

    private boolean isShow;

    public ReloginFun() {
    }

    public void execute() {

        if (CommonPopupWindowUtil.activities == null || CommonPopupWindowUtil.activities.size() <= 0) {
            return;
        }

        Page.ActivityPage activity = CommonPopupWindowUtil.activities.get(CommonPopupWindowUtil.activities.size() - 1);
        if (unShow(activity)) {
            return;
        }
        createDialog(activity);
        showDialog();
    }

    private void showDialog() {
        if (!isShow && alertDialog != null && !alertDialog.isShowing()) {
            isShow = true;
            alertDialog.show();
        }
    }

    private boolean unShow(Page.ActivityPage activity) {
//        return activity instanceof SelectURLActivity || activity instanceof GuideActivity
//                || activity instanceof SplashActivity || activity instanceof InputPhoneActivity
//                || activity instanceof LoginWithPwdActivity || activity instanceof LoginRetrievePwdActivity;
        return true;
    }

    private void createDialog(final Page.ActivityPage activity) {
        alertDialog = new AlertDialog(activity.activity()).builder()
                .setMsg("登录状态已过期,请重新登录")
                .setPositiveBold().setPositiveColor(R.color.theme_color).setPositiveButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        UserCenter.instance().toLogin(activity);
                        clear();
                    }
                }).setNegativeColor(R.color.color_999999).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isShow = false;
                        clear();
                    }
                })
                .setcCancelLisenter(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        isShow = false;
                        clear();
                    }
                });
    }

    private void clear() {
        alertDialog = null;
    }
}
