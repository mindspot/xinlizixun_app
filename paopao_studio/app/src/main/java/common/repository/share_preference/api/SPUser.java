package common.repository.share_preference.api;

import android.content.Context;
import android.content.SharedPreferences;

import common.repository.http.entity.user.UserInfoBean;
import common.repository.share_preference.SPBase;


/**
 * Created by Administrator on 2017/3/31.
 * 用户相关的
 */
public class SPUser extends SPBase {

    private static final String NAME = "user";

    private static SPUser instance;

    public static void init(Context context) {
        instance = new SPUser(context);
    }

    private SPUser(Context context) {
        super(context, NAME);
    }

    public static SPUser instance() {
        return instance;
    }

    /**
     * 用户名:phone
     */
    private static final String KEY_USER_PHONE = "user_phone";
    /**
     * sessionId
     */
    private static final String KEY_SESSIONID = "sessionid";
    /**
     * uid
     */
    private static final String KEY_UID = "uid";
    /**
     * 用户信息
     */
    private static final String KEY_USER_INFO = "user_info";

    /**
     * 实名姓名
     */
    private static final String KEY_LOGIN_REALNAME = "login_realname";

    /**
     * 签名
     */
    private static final String KEY_LOGIN_SIGN = "login_user_sign";

    /**
     * 头像
     */
    private static final String KEY_LOGIN_PHOTO = "login_user_photo";
    /**
     * 及时通讯账号
     */
    private static final String KEY_USER_IM_ACCOUNT = "user_im_account";


    public void setUserInfoBean(UserInfoBean user) {
        final boolean isClearUserInfo = user == null;
        SharedPreferences.Editor editor = edit();

        editor.putString(KEY_USER_PHONE, isClearUserInfo ? "" : user.getPhone());
        editor.putString(KEY_SESSIONID, isClearUserInfo ? "" : user.getToken());
        editor.putString(KEY_UID, isClearUserInfo ? "" : user.getUserId() + "");
        editor.putString(KEY_LOGIN_REALNAME, isClearUserInfo ? "" : user.getRealName());
        editor.putString(KEY_LOGIN_SIGN, isClearUserInfo ? "" : user.getSendWord());
        editor.putString(KEY_LOGIN_PHOTO, isClearUserInfo ? "" : user.getHeadImg());
        editor.putString(KEY_USER_IM_ACCOUNT, isClearUserInfo ? "" : user.getEasemobId());
        editor.apply();
    }

    public String getUserPhone() {
        return sp.getString(KEY_USER_PHONE, "");
    }

    public void setUserPhone(String username) {
        edit().putString(KEY_USER_PHONE, username).apply();
    }

    public String getSessionId() {
        return sp.getString(KEY_SESSIONID, "");
    }

    public void setSessionId(String sessionId) {
        edit().putString(KEY_SESSIONID, sessionId).apply();
    }

    public String getUID() {
        return sp.getString(KEY_UID, "");
    }

    public void setUID(String uid) {
        edit().putString(KEY_UID, uid).apply();
    }


    public String getUserInfo() {
        return sp.getString(KEY_USER_INFO, "");
    }

    public void setUserInfo(String userInfo) {
        edit().putString(KEY_USER_INFO, userInfo).apply();
    }

    public String getLoginRealName() {
        return sp.getString(KEY_LOGIN_REALNAME, "");
    }

    public void setLoginRealName(String realName) {
        edit().putString(KEY_LOGIN_REALNAME, realName).apply();
    }

    public String getSendWork() {
        return sp.getString(KEY_LOGIN_SIGN, "");
    }

    public void setSendWork(String sendWork) {
        edit().putString(KEY_LOGIN_SIGN, sendWork).apply();
    }

    public String getUserPhoto() {
        return sp.getString(KEY_LOGIN_PHOTO, "");
    }

    public void setUserPhoto(String url) {
        edit().putString(KEY_LOGIN_PHOTO, url).apply();
    }


    public String getUserImAccount() {
        return sp.getString(KEY_USER_IM_ACCOUNT, "");
    }

    public void setUserImAccount(String url) {
        edit().putString(KEY_USER_IM_ACCOUNT, url).apply();
    }
}
