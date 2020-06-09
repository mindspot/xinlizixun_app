package common.repository.share_preference.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.framework.utils.ConvertUtil;

import common.repository.share_preference.SPBase;
import module.app.MyApplication;


/**
 * Created by Administrator on 2017/3/31.
 * 下发的配置
 */
public class SPConfig extends SPBase {

    private static final String NAME = "config";

    public SPConfig(Context context) {
        super(context, NAME);
    }


    private static final String KEY_APP_NAME = "name";//add by wuyan

    private static final String KEY_UPDATE_BEAN = "update_bean";// update_msg
    private static final String KEY_URL_NO_LOGIN_LQB_MONEY = "url_no_login_lqb_money";
    private static final String KEY_URL_NO_LOGIN_LQB_TEXT = "url_no_login_lqb_text";
    private static final String KEY_URL_NO_LOGIN_FZB_MONEY = "url_no_login_fzb_money";
    private static final String KEY_URL_NO_LOGIN_FZB_TEXT = "url_no_login_fzb_text";


    private static final String KEY_URL_FIND = "url_find";
    private static final String KEY_URL_SITE = "url_site";
    private static final String KEY_URL_HELP = "url_help";
    private static final String KEY_URL_ABOUT = "url_about";
    private static final String KEY_URL_INVITE = "url_invite";
    private static final String KEY_URL_CALL_CENTER = "url_call_center";
    private static final String KEY_URL_CALL_QQ_SERVICE = "url_call_qq_service";
    private static final String KEY_URL_INFO_CAPTURE_SCRIPT = "url_info_capture_script";// URL_ZFB_JS
    private static final String KEY_URL_MESSAGE_CENTER = "url_message_center";//url_activity
    private static final String KEY_URL_VOUCHER_HELPER = "url_voucher_helper";

    private static final String KEY_COOKIE_LIST = "cookie_list";

    private static final String KEY_INFO_CAPTURE_DOMAIN = "info_capture_domain";// WEBVIEW_HOST_STR

    private static final String KEY_IS_SHOW_NOTICE_POP = "is_show_notice_pop";
    private static final String KEY_IS_SHOW_NOTICE_AD = "is_show_notice_ad";

    private static final String KEY_SHOW_HOT_DOT_COUNT = "show_hot_dot_count";
    private static final String KEY_IS_SAFE_PROTOCOL_URL = "is_safe_protocol_url";

    private static final String KEY_SERVICE_MSG_REGISTER = "service_msg_register";//注册协议文案
    /**
     * app的logo链接
     */
    private static final String KEY_LOGO_URL = "appLogo";
    /**
     * 安全键盘icon链接
     */
    private static final String KEY_BOARD_ICON_URL = "keyBoardIconUrl";

    /**
     * 控制是否显示通知权限弹窗
     */
    private static final String KEY_SERVICE_IS_SHOW_NOTICE_INFO = "is_show_notice_info";

    //写在share中的KEY_URL_COMPARE
    private static final String KEY_CACHE_URLS = "cache_urls";

    private static final String KEY_IS_SHOW_HOT_DOT = "is_show_hot_dot";
    private static final String KEY_IS_CLICK_RED_DOT = "is_click_red_dot";


    private static final String KEY_BAIRONG_CID = "bairong_cid";//百融apicode

    private static final String KEY_SERVERTIME = "serverTime";//客服正常服务时间

    private static final String KEY_WEEK_SERVERTIME = "weekServerTime";//客服周末 节假日服务时间

    private static final String KEY_SHOW_TYPE = "show_type";//是否显示客服入口

    private static final String KEY_COUPON_SHOW_TYPE = "coupon_show_type";//优惠券入口 1是显示 0不显示

    private static final String KEY_IS_USE_GZIP = "is_use_gzip";

    private static final String KEY_CC_VERSION = "cc_version";//认证中心流程 1 原流程 2新流程

    /**
     * 进入home后打开任意页面
     */
    private static final String KEY_SERVICE_JUMP_JSON = "jumpJson";


    public String getAppName() {
        return sp.getString(KEY_APP_NAME, "");
    }

    public String getUpdateBean() {
        return sp.getString(KEY_UPDATE_BEAN, "");
    }

    public void setUpdateBean(String updateBean) {
        edit().putString(KEY_UPDATE_BEAN, updateBean).apply();
    }

    public String getUrlNoLoginLqbMoney() {
        return sp.getString(KEY_URL_NO_LOGIN_LQB_MONEY, "");
    }

    public String getUrlNoLoginLqbText() {
        return sp.getString(KEY_URL_NO_LOGIN_LQB_TEXT, "");
    }

    public String getUrlNoLoginFzbMoney() {
        return sp.getString(KEY_URL_NO_LOGIN_FZB_MONEY, "");
    }

    public String getUrlNoLoginFzbText() {
        return sp.getString(KEY_URL_NO_LOGIN_FZB_TEXT, "");
    }

    public String getUrlSite() {
        return sp.getString(KEY_URL_SITE, "");
    }

    public String getUrlFind() {
        return sp.getString(KEY_URL_FIND, "");
    }

    public String getUrlHelp() {
        return sp.getString(KEY_URL_HELP, "");
    }

    public String getUrlAbout() {
        return sp.getString(KEY_URL_ABOUT, "");
    }

    public String getUrlInvite() {
        return sp.getString(KEY_URL_INVITE, "");
    }

    public String getUrlCallcenter() {
        return sp.getString(KEY_URL_CALL_CENTER, "");
    }

    public String getUrlCallQqService() {
        return sp.getString(KEY_URL_CALL_QQ_SERVICE, "");
    }

    public String getUrlZfbJs() {
        return sp.getString(KEY_URL_INFO_CAPTURE_SCRIPT, "");
    }

    public String getUrlMessageCenter() {
        return sp.getString(KEY_URL_MESSAGE_CENTER, "");
    }

    public String getUrlVoucherHelper() {
        return sp.getString(KEY_URL_VOUCHER_HELPER, "");
    }

    public String getCookieList() {
        return sp.getString(KEY_COOKIE_LIST, "");
    }

    public String getWebviewHostStr() {
        return sp.getString(KEY_INFO_CAPTURE_DOMAIN, "");
    }

    public int getIsShowNoticePop() {
        return sp.getInt(KEY_IS_SHOW_NOTICE_POP, 0);
    }

    public int getIsShowNoticeAd() {
        return sp.getInt(KEY_IS_SHOW_NOTICE_AD, 0);
    }

    public int getIsShowHotDot() {
        return sp.getInt(KEY_SHOW_HOT_DOT_COUNT, 0);
    }

    public String getIsSafeProtocolUrl() {
        return sp.getString(KEY_IS_SAFE_PROTOCOL_URL, "");
    }

    public String getUrlCompare() {
        return sp.getString(KEY_CACHE_URLS, "");
    }

    public boolean getShowMyhotDot() {
        return sp.getBoolean(KEY_IS_SHOW_HOT_DOT, true);
    }

    public boolean getClickMyhotDot() {
        return sp.getBoolean(KEY_IS_CLICK_RED_DOT, true);
    }

    public void setClickMyhotDot(boolean clickMyhotDot) {
        edit().putBoolean(KEY_IS_CLICK_RED_DOT, clickMyhotDot).apply();
    }

    public String getBairongCid() {
        return sp.getString(KEY_BAIRONG_CID, "");
    }

    public String getCacheUrlMap() {
        return sp.getString(KEY_CACHE_URLS, "");
    }

    public void setCacheUrlMap(String cacheUrls) {
        edit().putString(KEY_CACHE_URLS, cacheUrls).apply();
    }

    public String getServiceMsgRegister() {
        return sp.getString(KEY_SERVICE_MSG_REGISTER, "");
    }

    public String getKeyServiceIsShowNoticeInfo() {
        return sp.getString(KEY_SERVICE_IS_SHOW_NOTICE_INFO, "");
    }

    public String getLogoUrl() {
        return sp.getString(KEY_LOGO_URL, "");
    }

    public String getKeyBoardIconUrl() {
        return sp.getString(KEY_BOARD_ICON_URL, "");
    }

    public String getServerTime() {
        return sp.getString(KEY_SERVERTIME, "");
    }

    public String getWeekSererTime() {
        return sp.getString(KEY_WEEK_SERVERTIME, "");
    }

    public int getShowType() {
        return sp.getInt(KEY_SHOW_TYPE, 0);
    }

    public int getCouponShowType() {
        return sp.getInt(KEY_COUPON_SHOW_TYPE, 0);
    }

    public boolean isUseGzip() {
        return sp.getBoolean(KEY_IS_USE_GZIP, false);
    }

    public int getCcVersion() {
        return sp.getInt(KEY_CC_VERSION, 1);
    }

    public String getKeyServiceJumpJson() {
        return sp.getString(KEY_SERVICE_JUMP_JSON, "");
    }
}
