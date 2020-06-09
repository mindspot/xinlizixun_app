package util;

public class ServiceConfig {

    public static String SERVICE_BASE_IP = "http://pp.psyooo.com";
    public static String SERVICE_BASE_URL = SERVICE_BASE_IP + ":8087";
    public static String SERVICE_UPLOAD_IMAGE_HOST_URL = SERVICE_BASE_IP + ":8080";
    public static String SERVICE_UPLOAD_IMAGE_URL = SERVICE_UPLOAD_IMAGE_HOST_URL + "/group1/upload";
    //首页
    public static final String SERVICE_HOME_URL = "/index";
    //文章
    public static final String SERVICE_ARTIVLES_URL = "/article/list";
    //咨询 头部
    public static final String SERVICE_COUNSEL_HEADER_URL = "/consultation/label";
    //咨询 列表
    public static final String SERVICE_COUNSEL_LIST_URL = "/v1/consultantWork/consultantList";
    //搜索咨询 列表
    public static final String SERVICE_SEARCH_COUNSEL_LIST_URL = "/v1/consultation/s";
    //咨询师信息
    public static final String SERVICE_COUNSEL_DETAIL_URL = "/v1/consultationWork/get";
    //收藏咨询师
    public static final String SERVICE_COUNSEL_INSERT_COLLECT_URL = "/v1/collection/insert";
    //取消收藏咨询师
    public static final String SERVICE_COUNSEL_DEL_COLLECT_URL = "/v1/collection";
    //优惠券
    public static final String SERVICE_COUPON_LIST_URL = "/v1/coupon/list";
    //登录
    public static final String SERVICE_LOGIN_URL = "/consultant/phoneLogin";
    //验证码
    public static final String SERVICE_GET_CODE_URL = "/consultantWork/get/code";
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
    public static final String SERVICE_GET_USERINFO_URL = "/v1/consultationWork/consultant/get";
    //保存用户信息
    public static final String SERVICE_SAVE_USERINFO_URL = "/v1/consultationWork/consultant/update";
    //获取订单说明
    public static final String SERVICE_ORDER_EXPLAIN_URL = "/v1/order/payTypeDetailedDescription";
    //套餐支付订单
    public static final String SERVICE_ORDER_TAOCAN_URL = "/v1/pay/setMeal";
    //获取预约时间
    public static final String SERVICE_GET_DATE_URL = "/v1/bookingTime/list";
    //购买套餐列表
    public static final String SERVICE_SETMEAL_LIST_URL = "/v1/consultantWork/setMeal";
    //反馈
    public static final String SERVICE_UPLOAD_FEEDBACK_URL = "/v1/proposal/insert";
    //工作安排
    public static final String SERVICE_WORK_DATE_PLAN_URL = "/v1/bookingTime/list";
    //设置全天休息
    public static final String SERVICE_SET_ALLDAY_REST_URL = "/v1/userScheduleStatus/insertAll";
    //修改排版计划
    public static final String SERVICE_UPDATE_WORK_PLAN_URL = "/v1/userScheduleStatus/insert";
    //可督导订单列表
    public static final String SERVICE_USE_COUNCILOR_LIST_URL = "/v1/consultationWork/order/list";
    //支付督导订单
    public static final String SERVICE_PAY_COUNCILOR_ORDER_URL = "/v1/consultationWork/dealUnifiedSupervisorOrder";
    //获取消息订单数
    public static final String SERVICE_ORDER_MSG_NUM_URL = "/v1/consultantWork/orderUnReadNum";
    //已读订单消息
    public static final String SERVICE_READ_ORDER_MSG_URL = "/v1/consultantWork/orderRead";
    //用户订单列表
    public static final String SERVICE_USER_ORDER_URL = "/v1/consultantWork/order";
    //用户订单详情
    public static final String SERVICE_USER_ORDER_DETAIL_URL = "/v1/consultantWork/orderDetails";
    //我发起的督导订单
    public static final String SERVICE_MY_COUNCILOR_ORDER_LIST_URL = "/v1/consultationWork/consultant/supervisorOrder";
    //我收到的督导订单
    public static final String SERVICE_USER_COUNCILOR_ORDER_LIST_URL = "/v1/consultationWork/supervisor/supervisorOrder";
    //用户正在督导的订单
    public static final String SERVICE_NOW_USER_COUNCILOR_ORDER_LIST_URL = "/v1/consultationWork/easemobs/order/list";
    //确认用户订单
    public static final String SERVICE_CONFIRM_USER_ORDER_URL = "/v1/consultantWork/confirm";
    //提交用户病例
    public static final String SERVICE_SUBMIT_USER_CASE_URL = "/v1/consultantOrderDiagnosis/update";
    //评价督导订单
    public static final String SERVICE_EVALIATE_COUNCILOR_ORDER_URL = "/v1/consultationWork/supervisor/diagnosis";
    //我发起的督导订单详情
    public static final String SERVICE_MY_COUNCILOR_ORDER_DETAIL_URL = "/v1/consultationWork/supervisor/launchDetails";
    //我收到的督导订单详情
    public static final String SERVICE_USER_COUNCILOR_ORDER_DETAIL_URL = "/v1/consultationWork/supervisor/receivedDetails";
    //取消督导订单
    public static final String SERVICE_CANCEL_COUNCILOR_ORDER_URL = "/v1/consultationWork/supervisorOrder/cancel";
    //完成督导订单
    public static final String SERVICE_FINSH_COUNCILOR_ORDER_URL = "/v1/consultationWork/supervisorOrder/end";
    //拒绝督导订单
    public static final String SERVICE_REFUSE_COUNCILOR_ORDER_URL = "/v1/consultationWork/supervisorOrder/confirm";
    //接受督导订单
    public static final String SERVICE_ACCEPT_COUNCILOR_ORDER_URL = "/v1/consultationWork/supervisorOrder/confirm";
    //设置系统规定时间
    public static final String SERVICE_GET_SYSTEM_PLAN_TIME_URL = "/v1/platformWorkingTime/list";
    //我的页面数据
    public static final String SERVICE_MY_CENTER_DATE_URL = "/v1/consultationWork/getConsultant";
    //咨询师自定义工作时间
    public static final String SERVICE_SAVE_PLAN_TIME_URL = "/v1/userSchedule/insert";
    //获取用户可提现余额
    public static final String SERVICE_GET_USER_YUE_URL = "/v1/consultationWork/user/account";
    //获取用户卡列表
    public static final String SERVICE_GET_USER_CARD_LIST_URL = "/v1/consultationWork/card/list";
    //保存用户卡信息
    public static final String SERVICE_SAVE_USER_CARD_INFO_URL = "/v1/consultationWork/card/insert";
    //获取提现code
    public static final String SERVICE_GET_WITHDRAW_CODE_URL = "/v1/consultationWork/cashWithdrawal/getCode";
    //提现
    public static final String SERVICE_WITHDRAW_URL = "/v1/consultationWork/cashWithdrawal/insert";
    //提现记录
    public static final String SERVICE_WITHDRAW_RECORD_LIST_URL = "/v1/consultationWork/cashWithdrawal/list";
    //获取聊天状态
    public static final String SERVICE_CHAT_STATUS_URL = "/v1/consultationWork/conversation";
    //上报位置信息
    public static final String SERVICE_UPLOAD_AADDRESS_INFO_URL = "";
    //刷新Token
    public static final String SERVICE_REFRESH_TOKEN_URL = "/get/token";

    //入住Url
    public static final String SERVICE_COUNSEL_RUZHU_URL = SERVICE_BASE_IP + ":8097/#/login";
    //获取筛选城市
    public static final String SERVICE_CITY_INFOS_URL = "/v1/dictArea/list";
    //客服电话
    public static final String SERVICE_SERVICE_PHONE_URL = "/common/listCustomeService";
    //公司介绍
    public static final String SERVICE_COMPANY_INFO_URL = "/v1/common/companyIntroduction";
    //服务地址
    public static final String SERVICE_SERVICE_URL_URL = "/common/serviceUrl";
    //访客记录
    public static final String SERVICE_VISITOR_RECORD_LIST_URL = "/v1/visitor/list";

    //版本信息
    public static final String SERVICE_UPDATE_APP_URL = "/appVersion";
    //订单
    public static final String SERVICE_CONSUME_RECORD_URL = "/v1/point-history/list";

    public static String getUrl(String url, String param) {
        //url = HttpApi.getUserTokenUrl(url);
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
