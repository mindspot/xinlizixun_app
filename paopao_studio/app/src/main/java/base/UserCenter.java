package base;

import android.content.Context;

import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.hyphenate.chat.EMClient;

import common.repository.http.entity.user.UserInfoBean;
import common.repository.share_preference.SPApi;
import common.webview.custom.util.BCookieManager;
import im.IMHolder;
import module.user.LoginActivity;
import util.CommonPopupWindowUtil;

/**
 * Created by Administrator on 2017/8/16.
 * 用户中心
 */
public class UserCenter {

    private boolean hasLogin = false;

    public static UserCenter instance() {
        return Helper.INSTANCE;
    }

    private static final class Helper {
        static final UserCenter INSTANCE = new UserCenter();
    }

    private UserCenter() {
    }

    private UserInfoBean user;

    /**
     * 用户的登陆态
     */
    private boolean isLogin = false;

    //获取用户当前登录状态
    public boolean getLoginStatus() {
        return isLogin;
    }

//********************** ConfigUtil **********************

    public void initUserInfo() {
        setUserInfo(getUserInfo());
    }

    public UserInfoBean getUserInfo() {
        if (user == null) {
            // 缓存user，这样不需要每次都将json字符串转换为object
            user = ConvertUtil.toObject(SPApi.user().getUserInfo(), UserInfoBean.class);
        }
        return user;
    }

    public int getUid() {
        try {
            return getUserInfo().getUserId();
        } catch (Exception ignore) {
            return 0;
        }
    }

    private void setUserInfo(UserInfoBean userInfo) {
        this.user = userInfo;
        isLogin = (userInfo != null);
        SPApi.user().setUserInfo(ConvertUtil.toJsonString(userInfo));

    }


    public void toLogin(Page page) {
        if (hasLogin) {
            return;
        }
        hasLogin = true;
        LoginActivity.newIntent(page);
        page.activity().finish();
        for (Page.ActivityPage item : CommonPopupWindowUtil.activities) {
            item.activity().finish();
        }
    }

    /**
     * 登陆成功后保存用户信息
     */
    public void saveUserInfo(Page page, UserInfoBean userInfo) {
        hasLogin = false;
        if (userInfo == null) {
            return;
        }
        SPApi.user().setUserInfoBean(userInfo);
        setUserInfo(userInfo);
        BCookieManager.setCookie(userInfo, page.context());
        //此方法传入一个字符串String类型的参数，返回成功或失败的一个Boolean类型的返回值
        EMClient.getInstance().updateCurrentUserNick(userInfo.getRealName());
    }

    /**
     * 退出
     */
    public void logout(Page page) {
        clearLoginStatus(page.context());
        SPApi.clear();
        toLogin(page);
        EMClient.getInstance().logout(true);
        IMHolder.getInstance().setLoginEM(false);
    }

    /************
     * 清除登录状态
     */
    private void clearLoginStatus(Context context) {
        SPApi.user().setUserInfoBean(null);
        setUserInfo(null);
        BCookieManager.clearCookie(context);
    }
}
