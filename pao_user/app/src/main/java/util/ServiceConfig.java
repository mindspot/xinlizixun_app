package util;

public class ServiceConfig {

    public static String SERVICE_BASE_IP = "http://pp.psyooo.com";
    public static String SERVICE_BASE_URL = SERVICE_BASE_IP + ":8087";
    public static String SERVICE_UPLOAD_IMAGE_URL = SERVICE_BASE_IP + ":8080/group1/upload";
    //首页
    public static final String SERVICE_HOME_URL = "/v1/index";
    //文章
    public static final String SERVICE_ARTIVLES_URL = "/v1/article/list";
    //咨询 头部
    public static final String SERVICE_COUNSEL_HEADER_URL = "/consultation/label";
    //咨询 列表
    public static final String SERVICE_COUNSEL_LIST_URL = "/v1/consultation/list";
    //搜索咨询 列表
    public static final String SERVICE_SEARCH_COUNSEL_LIST_URL = "/v1/consultation/s";
    //咨询师信息
    public static final String SERVICE_COUNSEL_DETAIL_URL = "/v1/consultation/get";
    //收藏咨询师
    public static final String SERVICE_COUNSEL_INSERT_COLLECT_URL = "/v1/collection/insert";
    //取消收藏咨询师
    public static final String SERVICE_COUNSEL_DEL_COLLECT_URL = "/v1/collection";
    //优惠券
    public static final String SERVICE_COUPON_LIST_URL = "/v1/coupon/list";
    //登录
    public static final String SERVICE_LOGIN_URL = "/user/phoneLogin";
    //验证码
    public static final String SERVICE_GET_CODE_URL = "/get/code";
    //订单
    public static final String SERVICE_ORDER_LIST_URL = "/v1/order/list";
    //收藏
    public static final String SERVICE_COLLECT_LIST_URL = "/v1/collection/list";
    //普通订单
    public static final String SERVICE_CONSULTANT_ORDER_URL = "/v1/order/consultantOrder";
    //套餐订单
    public static final String SERVICE_TAO_CONSULTANT_ORDER_URL = "/v1/order/consultantSetMealOrder";
    //订单用户信息
    public static final String SERVICE_ORDER_USERINFO_URL = "/v1/order/materials";
    //获取用户信息
    public static final String SERVICE_GET_USERINFO_URL = "/v1/user/update";
    //保存用户信息
    public static final String SERVICE_SAVE_USERINFO_URL = "/v1/user/update";
    //获取订单说明
    public static final String SERVICE_ORDER_EXPLAIN_URL = "/v1/order/payTypeDetailedDescription";
    //套餐支付订单
    public static final String SERVICE_ORDER_TAOCAN_URL = "/v1/pay/setMeal";
    //获取预约时间
    public static final String SERVICE_GET_DATE_URL = "/v1/bookingTime/list";
    //购买套餐列表
    public static final String SERVICE_SETMEAL_LIST_URL = "/v1/order/setMeal/list";
    //反馈
    public static final String SERVICE_UPLOAD_FEEDBACK_URL = "/v1/proposal/insert";
    //支付宝支付
    public static final String SERVICE_ALI_PAY_INFO_URL = "/v1/pay/order";
    //获取聊天状态
    public static final String SERVICE_CHAT_STATUS_URL = "/v1/conversation";
    //上报位置信息
    public static final String SERVICE_UPLOAD_AADDRESS_INFO_URL = "";
    //保存用户卡信息
    public static final String SERVICE_SAVE_USER_CARD_INFO_URL = "/v1/consultationWork/card/insert";
    //获取用户可提现余额
    public static final String SERVICE_GET_USER_YUE_URL = "/v1/consultationWork/user/account";
    //获取用户卡列表
    public static final String SERVICE_GET_USER_CARD_LIST_URL = "/v1/consultationWork/card/list";
    //提现记录
    public static final String SERVICE_WITHDRAW_RECORD_LIST_URL = "/v1/consultationWork/cashWithdrawal/list";
    //获取提现code
    public static final String SERVICE_GET_WITHDRAW_CODE_URL = "/v1/consultationWork/cashWithdrawal/getCode";
    //提现
    public static final String SERVICE_WITHDRAW_URL = "/v1/consultationWork/cashWithdrawal/insert";
    //刷新Token
    public static final String SERVICE_REFRESH_TOKEN_URL = "/get/token";
    //取消订单
    public static final String SERVICE_CANCLE_ORDER_URL = "/v1/order/canceOrder";

    //获取筛选城市
    public static final String SERVICE_CITY_INFOS_URL = "/v1/dictArea/list";
    //客服电话
    public static final String SERVICE_SERVICE_PHONE_URL = "/common/listCustomeService";

    //公司介绍
    public static final String SERVICE_COMPANY_INFO_URL = "/v1/common/companyIntroduction";
    //服务地址
    public static final String SERVICE_SERVICE_URL_URL = "/common/serviceUrl";

    //版本信息
    public static final String SERVICE_UPDATE_APP_URL = "/appVersion";

    //订单
    public static final String SERVICE_CONSUME_RECORD_URL = "/v1/point-history/list";

    //咨询师是否在线
    public static final String SERVICE_CHAT_USER_LINE_STATUS_URL = "/v1/user/status";

    //提醒咨询师上线
    public static final String SERVICE_REMIND_CHAT_USER_LINE_URL = "/v1/sendSms";

    public static String getUrl(String url, String param) {
//        url = HttpApi.getUserTokenUrl(url);
        if (!url.contains("?")) {
            url = url + "?";
        } else {
            url = url + "&";
        }
        url = url + param;
        return url;
    }

    public static String getUrl(String url) {
        return SERVICE_BASE_URL + url;
    }
}
