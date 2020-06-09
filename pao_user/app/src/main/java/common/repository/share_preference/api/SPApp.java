package common.repository.share_preference.api;

import android.content.Context;
import android.support.annotation.IntDef;

import com.framework.utils.ConvertUtil;
import com.framework.utils.StringUtil;
import com.paopao.paopaouser.BuildConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import common.repository.share_preference.SPApi;
import common.repository.share_preference.SPBase;

/**
 * Created by Administrator on 2017/3/31.
 * APP全局相关的
 */
public class SPApp extends SPBase {

    private static final String NAME = "app";

    public SPApp(Context context) {
        super(context, NAME);
    }

    //判断是不是第一次进app 是的话暂时引导页
    private static final String KEY_FIRST_IN = "first_login";
    public static final int IS_FIRST_IN = 1;//首次
    public static final int NOT_FIRST_IN = -1;

    private static final String KEY_SEARCH_HISTORY = "key_search_history";

    @IntDef({IS_FIRST_IN, NOT_FIRST_IN})
    @Retention(RetentionPolicy.SOURCE)
    @interface FirstInState {
    }

    private static final String KEY_VERSION_NAME = "versionName";//版本名的key值

    private static final String KEY_IS_LOAD_CONFIG = "is_load_config"; //下拉配置状态

    private static final String KEY_NOTICE_IMG_URL = "notice_img_url";
    private static final String KEY_NOTICE_ACTION_URL = "notice_action_url";

    private static final String KEY_XGPUSH_URL = "xgpush_url";

    private static final String KEY_IS_SHOW_FUTURE_POPUP = "is_show_future_popup";

    private static final String KEY_MAIN_ACTION_POP_SYSTEM_CURRENT_TIME = "main_action_pop_system_current_time";

    private static final String KEY_IS_SETUP_JPUSH_ALIAS = "isSetupJpushAlias";// 是否注册了极光推送，废弃掉不用了

    private static final String KEY_CONFIG_DEVELOPER = "config_developer";// 后台开发人员的本机分支名称

    private static final String KEY_INDEX_ACTIVITY_HINT = "indexActivityHint";//首页活动弹框 上次id


    private static final String KEY_HOME_HOME_POP_INFOS = "home_pop_infos";//首页弹窗 信息:存储了弹出过的弹窗数据；对应的java bean是HomePopStorage

    private static final String KEY_CALENDAR_UNIQUE = "calendar_remind";//记录用户日历提醒的唯一标识

    private static final String KEY_FIRST_CC_PROCESS = "first_certification_process";//第一次认证流程

    private static final String KEY_NOW_LEND_BTN = "nowLendBtn";//"马上借款"按钮跳转到申请借款主页时，屏蔽非点击事件埋点

    private static final String KEY_FIRST_CC_MOBILE = "first_certification_mobile_url";//第一次认证流程,手机运营商url
    private static final String KEY_URL_OTHER = "url_other";
    /**
     * 是否为第一次登陆成功，第一次默认验证码登录，之后默认密码登录
     */
    private static final String KEY_IS_FIRST_LOGIN = "key_is_first_login";

    /**
     * 记录消息中心 点击时间点
     */
    private static final String KEY_UCENTER_MESSAGE_TIMESTAMP = "key_ucenter_message_timestamp";
    /**
     * 记录消息中心 心口字点的ID列表
     */
    private static final String KEY_UCENTER_MESSAGE_ID_LIST = "key_ucenter_message_id_list";

    /**
     * 是否有获取通讯录的权限
     *
     * @return
     */
    private static final String KEY_IS_HAVE_GET_CONTACTS_PERMISSION = "key_is_have_get_contacts_permission";

    /**
     * 是否打开过授权页面
     *
     * @return
     */
    private static final String KEY_IS_OPEN_ACCREDIT_PAGE = "key_is_open_accredit_page";
    /**
     * 导流首页GID 上一次生成的时间
     *
     * @return
     */
    private static final String KEY_DIVERSION_GID_PRODUCE_DATE = "key_diversion_gid_produce_date";
    /**
     * 导流首页GID
     *
     * @return
     */
    private static final String KEY_DIVERSION_PRODUCE_GID = "key_diversion_produce_gid";

    /**
     * 是否打开过人脸、身份证识别 提示
     *
     * @return
     */
    private static final String KEY_IS_OPEN_FACE_AND_IDCARD_DISCERN_PAGE = "key_is_open_face_and_idcard_discern";

    /**
     * 协议弹框
     *
     * @return
     */
    private static final String KEY_IS_OPEN_AGREEMENT_DIALOG = "key_is_open_agreement_dialog";

    public boolean isFirstIn() {
        int firstInState = sp.getInt(KEY_FIRST_IN, IS_FIRST_IN);
        return firstInState == IS_FIRST_IN;
    }

    public void setFirstInState(@FirstInState int firstInState) {
        edit().putInt(KEY_FIRST_IN, firstInState).apply();
    }

    /**
     * 判断版本名称是否相同
     *
     * @return boolean
     */
    public boolean isSameVersionName() {
        String string = sp.getString(KEY_VERSION_NAME, "");
        if (string.equals(BuildConfig.VERSION_NAME)) {
            return true;
        }
        return false;
    }

    /**
     * 写入版本名称
     */
    public void setVersionName() {
        edit().putString(KEY_VERSION_NAME, BuildConfig.VERSION_NAME).commit();
    }

    public boolean isLoadConfig() {
        return sp.getBoolean(KEY_IS_LOAD_CONFIG, false);
    }

    public void setLoadConfig(boolean isLoadConfig) {
        edit().putBoolean(KEY_IS_LOAD_CONFIG, isLoadConfig).apply();
    }

    public String getNoticeImageUrl() {
        return sp.getString(KEY_NOTICE_IMG_URL, "");
    }

    public void setNoticeImageUrl(String noticeImageUrl) {
        edit().putString(KEY_NOTICE_IMG_URL, noticeImageUrl).apply();
    }

    public String getNoticeActionUrl() {
        return sp.getString(KEY_NOTICE_ACTION_URL, "");
    }

    public void setNoticeActionUrl(String noticeActionUrl) {
        edit().putString(KEY_NOTICE_ACTION_URL, noticeActionUrl).apply();
    }

    public String getXgPushUrl() {
        return sp.getString(KEY_XGPUSH_URL, "");
    }

    public void setXgPushUrl(String xgPushUrl) {
        edit().putString(KEY_XGPUSH_URL, xgPushUrl).apply();
    }

    public boolean isShowFuturePopup() {
        return sp.getBoolean(KEY_IS_SHOW_FUTURE_POPUP, false);
    }

    public void setShowFuturePopup(boolean isShowFuturePopup) {
        edit().putBoolean(KEY_IS_SHOW_FUTURE_POPUP, isShowFuturePopup).apply();
    }

    public String getMainActionPopSystemCurrentTime() {
        return sp.getString(KEY_MAIN_ACTION_POP_SYSTEM_CURRENT_TIME, "");
    }

    public void setMainActionPopSystemCurrentTime(String mainActionPopSystemCurrentTime) {
        edit().putString(KEY_MAIN_ACTION_POP_SYSTEM_CURRENT_TIME, mainActionPopSystemCurrentTime).apply();
    }

    public boolean isSetupJpushAlias() {
        return sp.getBoolean(KEY_IS_SETUP_JPUSH_ALIAS, false);
    }

    public void setSetupJpushAlias(boolean isSetupJpushAlias) {
        edit().putBoolean(KEY_IS_SETUP_JPUSH_ALIAS, isSetupJpushAlias).apply();
    }

    public String getConfigDeveloper() {
        return sp.getString(KEY_CONFIG_DEVELOPER, "http://106.14.218.35:8087");
    }

    public void setConfigDeveloper(String dev) {
        edit().putString(KEY_CONFIG_DEVELOPER, dev).apply();
    }

    public int getIndexActivityHint() {
        return sp.getInt(KEY_INDEX_ACTIVITY_HINT, -1);
    }

    public void setIndexActivityHint(int dev) {
        edit().putInt(KEY_INDEX_ACTIVITY_HINT, dev).apply();
    }

    public String getHomePopInfos() {
        return sp.getString(KEY_HOME_HOME_POP_INFOS, "");
    }

    public void setHomePopInfos(String json) {
        edit().putString(KEY_HOME_HOME_POP_INFOS, json).apply();
    }


    public void setCalendarRemindUnique(String uniqueid) {
        edit().putString(KEY_CALENDAR_UNIQUE + getUid(), uniqueid).apply();
    }

    public String getCalendarRemindUnique() {
        return sp.getString(KEY_CALENDAR_UNIQUE + getUid(), null);
    }

    private String getUid() {
        return SPApi.user().getUID();
    }

    public int getFisrtCCProcess() {
        return sp.getInt(KEY_FIRST_CC_PROCESS, 1);
    }

    public void setFirstCCProcess(int status) {
        edit().putInt(KEY_FIRST_CC_PROCESS, status).apply();
    }


    public String getFisrtCCMobileUrl() {
        return sp.getString(KEY_FIRST_CC_MOBILE, "");
    }

    public void setFirstCCMobileUrl(String url) {
        edit().putString(KEY_FIRST_CC_MOBILE, url).apply();
    }

    public boolean isFirstLogin() {
        return sp.getBoolean(KEY_IS_FIRST_LOGIN, true);
    }

    public void setFirstLogined() {
        edit().putBoolean(KEY_IS_FIRST_LOGIN, false).apply();
    }

    public String getMessageTimeStamp() {
        return sp.getString(KEY_UCENTER_MESSAGE_TIMESTAMP, "");
    }

    public void setMessageTimeStamp(String timestamp) {
        edit().putString(KEY_UCENTER_MESSAGE_TIMESTAMP, timestamp).apply();
    }

    public String getMessageIdList() {
        return sp.getString(KEY_UCENTER_MESSAGE_ID_LIST, "");
    }

    public void setMessageIdList(String idlist) {
        edit().putString(KEY_UCENTER_MESSAGE_ID_LIST, idlist).apply();
    }

    /**
     * 记录visit_id的时间戳，登录成功和退出登录，打开APP时更新的
     */
    private static final String KEY_VISITID_TIMESTAMP = "key_visit_id_timestamp";

    public String getVisitIdTimeStamp() {
        return sp.getString(KEY_VISITID_TIMESTAMP, "");
    }

    public void setVisitIdTimeStamp(String timestamp) {
        edit().putString(KEY_VISITID_TIMESTAMP, timestamp).apply();
    }

    public boolean isHaveGetContactsPermission() {
        return sp.getBoolean(KEY_IS_HAVE_GET_CONTACTS_PERMISSION, false);
    }

    public void setIsHaveGetContactsPermission() {
        edit().putBoolean(KEY_IS_HAVE_GET_CONTACTS_PERMISSION, true).apply();
    }

    public boolean isOpenAccreditPage() {
        return sp.getBoolean(KEY_IS_OPEN_ACCREDIT_PAGE, false);
    }

    public void setOpenAccreditPage() {
        edit().putBoolean(KEY_IS_OPEN_ACCREDIT_PAGE, true).apply();
    }

    public boolean isOpenFaceAndIdCarDiscernPage() {
        return sp.getBoolean(KEY_IS_OPEN_FACE_AND_IDCARD_DISCERN_PAGE, false);
    }

    public void setOpenFaceAndIdCarDiscernPage() {
        edit().putBoolean(KEY_IS_OPEN_FACE_AND_IDCARD_DISCERN_PAGE, true).apply();
    }

    public String getDiversionGidProduceDate() {
        return sp.getString(KEY_DIVERSION_GID_PRODUCE_DATE, "");
    }

    public void setDiversionGidProduceDate(String date) {
        edit().putString(KEY_DIVERSION_GID_PRODUCE_DATE, date).apply();
    }

    public boolean getAgreementDialog() {
        return sp.getBoolean(KEY_IS_OPEN_AGREEMENT_DIALOG, false);
    }

    public void setAgreementDialog() {
        edit().putBoolean(KEY_IS_OPEN_AGREEMENT_DIALOG, true).apply();
    }

    public String getDiversionProduceGid() {
        return sp.getString(KEY_DIVERSION_PRODUCE_GID, "");
    }

    public void setDiversionProduceGid(String timestamp) {
        edit().putString(KEY_DIVERSION_PRODUCE_GID, timestamp).apply();
    }

    public List<String> getSearchRecord() {
        String history = sp.getString(KEY_SEARCH_HISTORY, "");
        List<String> list = new ArrayList<>();
        if (!StringUtil.isBlank(history)) {
            list.clear();
            List<String> result = ConvertUtil.toList(history, String.class);
            if (result != null && result.size() > 0)
                list.addAll(result);
        }
        return list;
    }

    public void setSearchRecord(String label) {
        List<String> list = getSearchRecord();
        if (list.contains(label)) {
            list.remove(label);
        }
        list.add(0, label);
        if (list.size() > 6) {
            list.remove(list.size() - 1);
        }
        edit().putString(KEY_SEARCH_HISTORY, ConvertUtil.toJsonString(list)).apply();
    }

    public void clearSearchRecotd() {
        edit().putString(KEY_SEARCH_HISTORY, "").apply();
    }
}
