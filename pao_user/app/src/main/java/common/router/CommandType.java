package common.router;

/**
 * @author Administrator
 * @date 2017/7/17
 */
public class CommandType {

    /**
     * 错误的跳转类型，不执行跳转或其他操作
     */
    public static final int TYPE_ERROR = -1;
    public static final String PATH_UNKNOWN = Path.UNKNOWN;

    /**
     * 返回原生
     */
    public static final int TYPE_BACK = 0;
    public static final String PATH_BACK = Path.App.BACK;
    /**
     * 忘记密码
     */
    public static final int TYPE_FORGET_PWD = 1;
    public static final String PATH_FORGET_PWD = Path.User.Password.Login.FORGET;
    /**
     * 忘记交易密码
     */
    public static final int TYPE_FORGET_PAYPWD = 2;
    public static final String PATH_FORGET_PAYPWD = Path.User.Password.Pay.FORGET;
    /**
     * 认证页面:完善资料-认证中心
     */
    public static final int TYPE_CC = 3;
    public static final String PATH_CC = Path.Certifice.CC;
    /**
     * 借款界面
     */
    public static final int TYPE_MAIN_LEND = 4;
    public static final String PATH_MAIN_LEND = Path.Main.LEND;
    /**
     * 老版本跳转QQ客服
     */
    public static final int TYPE_QQ = 5;
    /**
     * 显示红包，白条老逻辑
     */
    public static final int TYPE_SHOW_BENEFIT = 6;
    public static final String PATH_SHOW_BENEFIT = Path.H5.Webview.BENEFIT;
    /**
     * 返回首页
     */
    public static final int TYPE_MAIN = 7;
    public static final String PATH_MAIN = Path.Main.HOME;
    /**
     * 借款记录
     */
    public static final int TYPE_LOAN_RECORDS = 8;
    public static final String PATH_LOAN_RECORDS = Path.User.LOAN_RECORDS;
    /**
     * 借款记录
     */
    public static final int TYPE_NEW_LOAN_RECORDS = 28;
    public static final String PATH_NEW_LOAN_RECORDS = Path.User.NEW_LOAN_RECORDS;
    /**
     * 上传还款凭证
     */
    public static final int TYPE_UPLOAD_REPAY_VOUCHER = 9;
    public static final String PATH_UPLOAD_REPAY_VOUCHER = Path.H5.Webview.UPLOAD_REPAY_VOUCHER;
    /**
     * 打开浏览器
     */
    public static final int TYPE_OPEN_BROWSER = 10;
    public static final String PATH_OPEN_BROWSER = Path.App.OPEN_BROWSER;
    /**
     * 意见反馈：包含了意见反馈与催收投诉
     */
    public static final int TYPE_FEEDBACK = 11;
    public static final String PATH_FEEDBACK = Path.User.Feedback.FEEDBACK;
    /**
     * 新版本跳转QQ客服
     */
    public static final int TYPE_QQ_NEW = 12;
    public static final String PATH_QQ_NEW = Path.App.QQ;
    /**
     * 还款界面
     */
    public static final int TYPE_MAIN_REPAY = 13;
    public static final String PATH_MAIN_REPAY = Path.Main.REPAY;
    /**
     * 唤醒微信
     */
    public static final int TYPE_OPEN_WECHAT = 14;
    public static final String PATH_OPEN_WECHAT = Path.App.OPEN_WECHAT;
    /**
     * 跳转发现
     */
    public static final int TYPE_MAIN_FIND = 15;
    public static final String PATH_MAIN_FIND = Path.Main.FIND;
    /**
     * 跳转更多其他
     */
    public static final int TYPE_MAIN_OTHER = 21;
    public static final String PATH_MAIN_OTHER = Path.Main.OTHER;
    /**
     * 弹出拨打电话的dialog
     */
    public static final int TYPE_SHOW_CALL_PHONE = 16;
    public static final String PATH_SHOW_CALL_PHONE = Path.App.SHOW_CALL_PHONE;
    /**
     * 我的
     */
    public static final int TYPE_MY_CENTER = 17;
    public static final String PATH_MY_CENTER = Path.Main.MY_CENTER;
    /**
     * 分享
     */
    public static final int TYPE_SHARE = 18;
    public static final String PATH_SHARE = Path.App.SHARE;
    /**
     * webview页面中，右上角的点击功能
     */
    public static final int TYPE_WEBVIEW_TOP_RIGHT_BUTTON = 19;
    public static final String PATH_WEBVIEW_TOP_RIGHT_BUTTON = Path.H5.Webview.TOP_RIGHT_BUTTON;
    /**
     * 操作日志上报
     */
    public static final int TYPE_OPERATION_LOG = 20;
    public static final String PATH_OPERATION_LOG = Path.Data.OPERATION_LOG;
    /**
     * 百融
     */
    public static final int TYPE_BAI_RONG = 22;
    public static final String PATH_BAI_RONG = Path.Data.BAI_RONG;
    /**
     * 用户登陆
     */
    public static final int TYPE_USER_LOGIN = 23;
    public static final String PATH_USER_LOGIN = Path.User.LOGIN;
    /**
     * 对外导流登陆
     */
    public static final int TYPE_DIVERSION_USER_LOGIN = 24;
    public static final String PATH_DIVERSION_USER_LOGIN = Path.User.DIVERSION_LOGIN;

    /**
     * FaceOcr识别
     */
    public static final int TYPE_FACE_OCR = 25;
    public static final String PATH_FACE_OCR = Path.H5.FACE_OCR;

    /**
     * 跳转微信小程序
     */
    public static final int TYPE_WECHAT_MINI = 26;
    public static final String PATH_WECHAT_MINI = Path.App.GOTO_WECHAT_MINI;
    /**
     * 刷新tabList
     */
    public static final int TYPE_REFRESH_TABLIST = 27;
    public static final String PATH_REFRESH_TABLIST = Path.Main.REFRESH_TABLIST;
    /**
     * 个人信息认证
     */
    public static final int TYPE_AUTH_PERSION_INFO = 1001;
    public static final String PATH_AUTH_PERSION_INFO = Path.Certifice.PERSION;
    /**
     * 工作信息认证
     */
    public static final int TYPE_AUTH_WORK_INFO = 1002;
    public static final String PATH_AUTH_WORK_INFO = Path.Certifice.Work.WORK_AFTER_VERIFY;
    /**
     * 紧急联系人认证
     */
    public static final int TYPE_AUTH_CONTACT_INFO = 1003;
    public static final String PATH_AUTH_CONTACT_INFO = Path.Certifice.CONTACT;
    /**
     * 对外紧急联系人认证
     */
    public static final int TYPE_OPENAPI_AUTH_CONTACT_INFO = 6001;
    public static final String PATH_OPENAPI_AUTH_CONTACT_INFO = Path.Certifice.OPENAPI_CONTACT;
    /**
     * 银行信息认证
     */
    public static final int TYPE_AUTH_ADD_BANK_INFO = 1004;
    public static final String PATH_AUTH_ADD_BANK_INFO = Path.Certifice.Bank.ADD_AFTER_VERIFY;
    /**
     * 手机运营商认证
     */
    public static final int TYPE_AUTH_PHONE_INFO = 1005;
    public static final String PATH_AUTH_PHONE_INFO = Path.Certifice.Phone.OPERATOR_AFTER_VERIFY;
    /**
     * 卡片等级
     */
    public static final int TYPE_AUTH_CARD_LEVEL_INFO = 1006;
    public static final String PATH_AUTH_CARD_LEVEL_INFO = Path.Certifice.CARD_LEVEL;
    /**
     * 更多认证
     */
    public static final int TYPE_AUTH_MORE_INFO = 1007;
    /**
     * 芝麻信用认证
     */
    public static final int TYPE_AUTH_ZMXY_INFO = 1008;
    public static final String PATH_AUTH_ZMXY_INFO = Path.Certifice.Zmxy.ZMXY_AFTER_VERIFY;
    /**
     * 支付宝认证（魔蝎）
     */
    public static final int TYPE_AUTH_ALIPAY_INFO = 1009;
    public static final String PATH_AUTH_ALIPAY_INFO = Path.Certifice.Moxie.ALIPAY_NEED_VERIFY;
    /**
     * 淘宝认证
     */
    public static final int TYPE_AUTH_TAOBAO_INFO = 1010;
    /**
     * 工资卡认证
     */
    public static final int TYPE_AUTH_SALARY_INFO = 1012;
    public static final String PATH_AUTH_SALARY_INFO = Path.Certifice.SALARY;
    /**
     * 公积金认证(大额分期认证，需要先认证基础认证信息和工作信息)
     */
    public static final int TYPE_AUTH_AF_INFO = 1013;
    public static final String PATH_AUTH_AF_INFO = Path.Certifice.Accumulationfund.AUTH_VERIFY;
    /**
     * 社保卡认证
     */
    public static final int TYPE_AUTH_SOCIAL_INFO = 1014;
    public static final String PATH_AUTH_SOCIAL_INFO = Path.Certifice.Social.SOCIAL_AFTER_VERIFY;
    /**
     * 邮箱账单认证
     */
    public static final int TYPE_AUTH_EMAILBILLS_INFO = 1015;
    public static final String PATH_AUTH_EMAILBILLS_INFO = Path.Certifice.Moxie.EMAILBILLS_NEED_VERIFY;
    /**
     * 微信认证
     */
    public static final int TYPE_AUTH_WECHAT_INFO = 1016;
    /**
     * 绑定银行卡认证
     */
    public static final int TYPE_AUTH_BIND_BANK_INFO = 1017;
    /**
     * 账单认证
     */
    public static final int TYPE_AUTH_BILLS_INFO = 1018;
    /**
     * 网银认证
     */
    public static final int TYPE_AUTH_EBANK_BILLS_INFO = 1019;
    /**
     * 其他认证
     */
    public static final int TYPE_AUTH_OTHER_INFO = 1020;
    public static final String PATH_AUTH_OTHER_INFO = Path.Certifice.OTHER;
    /**
     * 公信宝认证
     */
    public static final int TYPE_AUTH_GXB_INFO = 1022;
    public static final String PATH_AUTH_GXB_INFO = Path.Certifice.Gxbao.GXBAO_AFTER_VERIFY;
    /**
     * 公积金认证（28天分期认证，需要先认证个人信息和工作信息）
     */
    public static final int TYPE_AUTH_INSTALMENT_AF_INFO = 1023;
    public static final String PATH_AUTH_28DAY_AF_INFO = Path.Certifice.Accumulationfund.AUTH_28DAY_VERIFY;

    /**
     * 打开通讯录，并回调
     */
    public static final int TYPE_GET_CONTACT_FROM_APP = 1024;
    public static final String PATH_GET_CONTACT_FROM_APP = Path.Data.GET_CONTACT_FROM_APP;

    /**
     * 流程认证中心
     */
    public static final int TYPE_FLOW_PATH_CC = 1025;
    public static final String PATH_FLOW_PATH_CC = Path.Certifice.FLOW_PATH_CC;
    /**
     * 人脸、身份证 识别
     */
    public static final int TYPE_AUTH_FACE_IDCARD_DISCERN = 1026;
    public static final String PATH_AUTH_FACE_IDCARD_DISCERN = Path.Certifice.FACE_IDCARD;
    /**
     * 新个人信息
     */
    public static final int TYPE_AUTH_NEW_PERSON_INFO = 1027;
    public static final String PATH_AUTH_NEW_PERSON_INFO = Path.Certifice.NEW_PERSION;
    /**
     * 帮助中心
     */
    public static final int TYPE_HELP_CENTER = 2004;
    /**
     * 我的优惠劵
     */
    public static final int TYPE_MY_DISCOUNT = 2005;
    public static final String PATH_MY_DISCOUNT = Path.User.MY_DISCOUNT;
    /**
     * 消息中心
     */
    public static final int TYPE_MESSAGE_CENTER = 2006;
    /**
     * 我的邀请
     */
    public static final int TYPE_MY_INVITATION = 2007;
    /**
     * 设置
     */
    public static final int TYPE_SET_MORE = 2008;
    public static final String PATH_SET_MORE = Path.User.OPEN_SETTING_PAGE;
    /**
     * 首页'我的'界面中的其他情况
     */
    public static final int TYPE_MORE_DEFAULT = 2009;
    /**
     * 现金红包
     */
    public static final int TYPE_MORE_XJHB = 2010;
    public static final String PATH_MORE_XJHB = Path.User.XJHB;

    /**
     * 意见反馈
     */
    public static final int TYPE_FEEDBACK_PRODUCT = 2101;
    public static final String PATH_FEEDBACK_PRODUCT = Path.User.Feedback.PRODUCT;
    /**
     * 催收投诉
     */
    public static final int TYPE_FEEDBACK_COLLECTION_COMPLAINT = 2102;
    public static final String PATH_FEEDBACK_COLLECTION_COMPLAINT = Path.User.Feedback.COLLECTION_COMPLAINT;
    /**
     * 跳转到APP内部的网页界面，打开URL
     */
    public static final int TYPE_URL = 2103;
    public static final String PATH_URL = Path.H5.URL;
    /**
     * 关于我们
     */
    public static final int TYPE_ABOUT_ME = 2104;
    /**
     * 修改登录密码
     */
    public static final int TYPE_UPDATE_LOGIN_PASS = 2105;
    public static final String PATH_UPDATE_LOGIN_PASS = Path.User.Password.Login.UPDATE;
    /**
     * 修改交易密码
     */
    public static final int TYPE_UPDATE_PAY_PASS = 2106;
    public static final String PATH_UPDATE_PAY_PASS = Path.User.Password.Pay.AUTO;

    /**
     * weex页面
     */
    public static final int TYPE_WEEX = 2108;
    public static final String PATH_WEEX = Path.App.WEEX;

    /**
     * 客服中心
     */
    public static final int TYPE_CUSTOMER_SERVICE_CENTER = 2012;
    public static final String PATH_CUSTOMER_SERVICE_CENTER = Path.App.CUSTOMER_SERVICE_CENTER;
    /**
     * weex 选择图片 回调，不公开，
     */
    public static final int TYPE_WEEX_PICK_IMAGE = -2;
    public static final String PATH_WEEX_PICK_IMAGE = Path.App.WEEX_PICK_IMAGE;
    /**
     * 抓取娃娃机 h5页面
     */
    public static final int TYPE_PRIZE_CLAW = 3001;
    /**
     * 横屏 h5游戏
     */
    public static final int TYPE_HORIZONTAL_GAME = 3002;
    public static final String PATH_HORIZONTAL_GAME = Path.H5.X5.HORIZONTAL;

    /**
     * 跳转判断放在后台进行 新增Type
     */
    /**
     * 手机运营商认证
     */
    public static final int JUMP_NOTVERIFY_PHONE_INFO = 4001;
    public static final String PATH_NOTVERIFY_PHONE_INFO = Path.Certifice.Phone.OPERATPR;
    /**
     * 芝麻信用认证
     */
    public static final int JUMP_NOTVERIFY_ZMXY_INFO = 4002;
    public static final String PATH_NOTVERIFY_ZMXY_INFO = Path.Certifice.Zmxy.ZMXY;
    /**
     * 银行信息认证
     */
    public static final int JUMP_NOTVERIFY_ADD_BANK_INFO = 4003;
    public static final String PATH_NOTVERIFY_ADD_BANK_INFO = Path.Certifice.Bank.ADD;
    /**
     * 工作信息认证
     */
    public static final int JUMP_NOTVERIFY_WORK_INFO = 4004;
    public static final String PATH_NOTVERIFY_WORK_INFO = Path.Certifice.Work.WORK;
    /**
     * 公积金认证
     */
    public static final int JUMP_NOTVERIFY_AF_INFO = 4005;
    public static final String PATH_NOTVERIFY_AF_INFO = Path.Certifice.Accumulationfund.AUTH;
    /**
     * 社保认证
     */
    public static final int JUMP_NOTVERIFY_SOCIAL_INFO = 4006;
    public static final String PATH_NOTVERIFY_SOCIAL_INFO = Path.Certifice.Social.SOCIAL;
    /**
     * 车险认证
     */
    public static final int JUMP_NOTVERIFY_INSURANCE_INFO = 4007;
    public static final String PATH_NOTVERIFY_INSURANCE_INFO = Path.Certifice.Moxie.INSURANCE;
    /**
     * 保单认证
     */
    public static final int JUMP_NOTVERIFY_LIFEINSURANCE_INFO = 4008;
    public static final String PATH_NOTVERIFY_LIFEINSURANCE_INFO = Path.Certifice.Moxie.LIFEINSURANCE;
    /**
     * 邮箱账单认证
     */
    public static final int JUMP_NOTVERIFY_EMAILBILLS_INFO = 4009;
    public static final String PATH_NOTVERIFY_EMAILBILLS_INFO = Path.Certifice.Moxie.EMAILBILLS;
    /**
     * 学信网认证
     */
    public static final int JUMP_NOTVERIFY_SCHOOLROLL_INFO = 4010;
    public static final String PATH_NOTVERIFY_SCHOOLROLL_INFO = Path.Certifice.SCHOOLROLL;
    /**
     * 支付宝认证
     */
    public static final int JUMP_NOTVERIFY_ALIPAY_INFO = 4011;
    public static final String PATH_NOTVERIFY_ALIPAY_INFO = Path.Certifice.Moxie.ALIPAY;
    /**
     * 设置交易密码
     */
    public static final int JUMP_NOTVERIFY_SETTING_PAYPASS = 4012;
    public static final String PATH_SET_PAYPASS = Path.User.Password.Pay.SET;
    /**
     * 修改交易密码
     */
    public static final int JUMP_NOTVERIFY_UPDATE_PAYPASS = 4013;
    public static final String PATH_SET_UPDATE_PAYPASS = Path.User.Password.Pay.UPDATE;
    /**
     * 退出登录
     */
    public static final int JUMP_NOTVERIFY_LOGOUT = 4014;
    public static final String PATH_LOGOUT = Path.User.LOGOUT;
    /**
     * 公信宝
     */
    public static final int JUMP_NOTVERIFY_GXBAO_INFO = 4015;
    public static final String PATH_NOTVERIFY_GXBAO_INFO = Path.Certifice.Gxbao.GXBAO;

    /**
     * 魔蝎认证
     */
    public static final int JUMP_MOXIE = 4016;
    public static final String PATH_MOXIE = Path.Certifice.Moxie.MOXIE;

    /**
     * 消息中心
     */
    public static final int TYPE_MY_MESSAGE = 4017;
    public static final String PATH_MY_MESSAGE = Path.User.OPEN_MESSAGE_PAGE;

    /**
     * 外部公积金
     */
    public static final int TYPE_OPEN_NOTVERIFY_AF_INFO = 4028;
    public static final String PATH_OPEN_NOTVERIFY_AF_INFO = Path.User.OPEN_NOTVERIFY_AF_INFO;
    /**
     * 强制登录
     */
    public static final int TYPE_FORCE_LOGIN = 4029;
    public static final String PATH_FORCE_LOGIN = Path.User.USER_FORCE_LOGIN;
    /**
     * Toast提示
     */
    public static final int JUMP_TIPS_TOAST = 5001;
    public static final String PATH_TIPS_TOAST = Path.Ui.Tip.TOAST;

    /**
     * Dialog提示
     */
    public static final int JUMP_TIPS_DIALOG = 5002;
    public static final String PATH_TIPS_DIALOG = Path.Ui.Tip.DIALOG;
    /**
     * 推送对话框
     */
    public static final int JUMP_TIPS_PUSH_DIALOG = 5003;
    public static final String PATH_TIPS_PUSH_DIALOG = Path.Ui.Tip.PUSH_DIALOG;
    /**
     * 推送通知栏
     */
    public static final int JUMP_TIPS_PUSH_NOTIFICATION = 5004;
    public static final String PATH_TIPS_PUSH_NOTIFICATION = Path.Ui.Tip.PUSH_NOTIFICATION;
    /**
     * 外部API 个人信息页
     */
    public static final int TYPE_AUTH_OPEN_API_PERSONAL = 6002;
    public static final String PATH_AUTH_OPEN_API_PERSONAL = Path.Certifice.OPEN_API_PERSION;


    /**
     * 埋点 设置页面名称
     */
    public static final int TYPE_GROWINGIO_SET_PAGENAME = 7005;
    public static final String PATH_GROWINGIO_SET_PAGENAME = Path.GrowingIO.PAGENAME;

    /**
     * growingIO埋点
     */
    public static final int TYPE_GROWINGIO_SET_BURIED_POINT = 7006;
    public static final String PATH_GROWINGIO_SET_BURIED_POINT = Path.GrowingIO.BURIED_POINT;

    public static final int TYPE_GROWINGIO_BI = 7007;
    public static final String PATH_GROWINGIO_BI = Path.GrowingIO.BI;

    /**
     * ali百川商品详情
     */
    public static final int TYPE_ALIBC_DETAIL = 8001;
    public static final String PATH_ALIBC_DETAIL = Path.ALIbc.BCDETAIL;
    /*
     * 支付宝支付
     */
    public static final int JUMP_ALI_PAY = 6003;
    public static final String PATH_ALI_PAY = Path.Pay.ALI_PAY;

    /**
     * 微信支付
     */
    public static final int JUMP_WX_PAY = 16001;
    public static final String PATH_WX_PAY = Path.Pay.WX_PAY;
    /**
     * 地址
     */
    public static final int TYPE_USER_ADDRESS_LIST = 16002;
    public static final String PATH_USER_ADDRESS_LIST = Path.User.Address.LIST;
    /**
     * 我的浏览记录
     */
    public static final int TYPE_PRODUCTS_CLASS_BROWSERECORD = 4024;
    public static final String PATH_PRODUCTS_CLASS_BROWSERECORD = Path.User.BROWSERECORD;
    /**
     * 浏览记录操作指令
     */
    public static final int TYPE_LOOK_OPERATE = 4025;
    public static final String PATH_LOOK_OPERATE = Path.User.LOOK_OPERATE;
    /**
     * 我的收藏
     */
    public static final int TYPE_PRODUCTS_CLASS_COLLECT = 4026;
    public static final String PATH_PRODUCTS_CLASS_COLLECT = Path.User.COLLECT;
    /**
     * 收藏操作指令
     */
    public static final int TYPE_COLLECTION_OPERATE = 4027;
    public static final String PATH_COLLECTION_OPERATE = Path.User.CLLECTION_OPERATE;

    /**
     * 商品详情
     */
    public static final int TYPE_PRODUCTS_DETAILS = 4018;
    public static final String PATH_PRODUCTS_DETAILS = Path.Product.PRODUCT_DETAIL;
    public static final int TYPE_PRODUCTS_LIST = 4019;
    public static final String PATH_PRODUCTS_LIST = Path.Product.PRODUCT_LIST;

    public static final int TYPE_PRODUCTS_CLASSIFY = 4020;
    public static final String PATH_PRODUCTS_CLASSIFY = Path.Product.PRODUCT_CLASSIFY;
    /**
     * 关键词搜索
     */
    public static final int TYPE_PRODUCTS_SEARCH = 4023;
    public static final String PATH_PRODUCTS_SEARCH = Path.Product.PRODUCT_SEARCH;

    /**
     * 操作购物车
     */
    public static final int TYPE_SHOPPING_CART_OPERATE = 7002;
    public static final String PATH_SHOPPING_CART_OPERATE = Path.ShoppingCart.OPERATE;

    /**
     * 购物车下单
     */
    public static final int TYPE_SHOPPING_CART_ORDERS = 7003;
    public static final String PATH_SHOPPING_CART_ORDERS = Path.ShoppingCart.ORDERS;

    /**
     * 购物车页面
     */
    public static final int TYPE_SHOPPING_CART_DATA_WINDOW = 7004;
    public static final String PATH_SHOPPING_CART_DATA_WINDOW = Path.ShoppingCart.WINDOW;


    public static final int TYPE_WEBVIEW_TOP_LEFT_BUTTON = 10025;
    public static final String PATH_WEBVIEW_TOP_LEFT_BUTTON = Path.H5.Webview.TOP_LEFT_BUTTON;

    public static final int TYPE_TRANSPARENT_WEBVIEW = 10026;
    public static final String PATH_TRANSPARENT_WEBVIEW = Path.H5.Webview.TRANSPARENT_WEBVIEW;

    public static final class Path {
        /**
         * 商品
         */
        public static final class Product {
            private static final String BASE = "/product";

            private static final String PRODUCT_DETAIL = BASE + "/detail";
            private static final String PRODUCT_LIST = BASE + "/list";
            private static final String PRODUCT_CLASSIFY = BASE + "/classify";
            private static final String PRODUCT_SEARCH = BASE + "/counsel_search_icon";
        }

        /**
         * 错误的跳转类型，不执行跳转或其他操作
         */
        public static final String UNKNOWN = "";


        /**
         * 阿里百川
         */
        public static final class ALIbc {
            private static final String BASE = "/alibc";
            /**
             * 调起百川详情
             */
            private static final String BCDETAIL = BASE + "/bc_detail";
        }

        /**
         * 用户
         */
        public static final class User {
            private static final String BASE = "/user";

            /**
             * 用户登陆
             */
            public static final String LOGIN = BASE + "/login";
            public static final String DIVERSION_LOGIN = BASE + "/login/diversion";

            /**
             * 退出登录
             */
            public static final String LOGOUT = BASE + "/logout";
            /**
             * 我的收藏
             */
            public static final String COLLECT = BASE + "/collect";
            /**
             * 我的浏览记录
             */
            public static final String BROWSERECORD = BASE + "/browserecord";
            /**
             * 我的收藏浏览记录操作
             */
            public static final String CLLECTION_OPERATE = BASE + "/collection/operate";
            /**
             * 我的浏览记录
             */
            public static final String LOOK_OPERATE = BASE + "/look/operate";

            /**
             * 密码
             */
            public static final class Password {
                private static final String BASE = User.BASE + "/password";

                /**
                 * 登录密码
                 */
                public static final class Login {
                    private static final String BASE = Password.BASE + "/login";
                    /**
                     * 忘记密码
                     */
                    public static final String FORGET = BASE + "/forget";
                    /**
                     * 修改登录密码
                     */
                    public static final String UPDATE = BASE + "/update";
                }

                /**
                 * 支付密码
                 */
                public static final class Pay {
                    private static final String BASE = Password.BASE + "/pay";

                    /**
                     * 忘记交易密码
                     */
                    public static final String FORGET = BASE + "/forget";
                    /**
                     * 修改或设置交易密码，根据参数自动选择
                     */
                    public static final String AUTO = BASE + "/auto";
                    /**
                     * 设置交易密码
                     */
                    public static final String SET = BASE + "/set";
                    /**
                     * 修改交易密码
                     */
                    public static final String UPDATE = BASE + "/update";
                }

            }

            /**
             * 反馈
             */
            public static final class Feedback {
                private static final String BASE = User.BASE + "/feedback";
                /**
                 * 意见反馈：包含了意见反馈与催收投诉
                 */
                public static final String FEEDBACK = BASE + "/";
                /**
                 * 意见反馈
                 */
                public static final String PRODUCT = BASE + "/product";
                /**
                 * 催收投诉
                 */
                public static final String COLLECTION_COMPLAINT = BASE + "/collection_complaint";
            }

            /**
             * 收货地址
             */
            public static final class Address {
                private static final String BASE = User.BASE + "/address";
                /**
                 * 收货地址列表
                 */
                public static final String LIST = BASE + "/list";
            }

            /**
             * 借款记录
             */
            public static final String LOAN_RECORDS = BASE + "/loan/records";
            /**
             * 借款记录
             */
            public static final String NEW_LOAN_RECORDS = BASE + "/loan/newrecords";
            /**
             * 我的优惠劵
             */
            public static final String MY_DISCOUNT = BASE + "/discount";
            /**
             * 现金红包
             */
            public static final String XJHB = BASE + "/xjhb";
            /**
             * 跳转到设置页面
             */
            public static final String OPEN_SETTING_PAGE = BASE + "/setting_page";
            /**
             * 跳转到消息中心
             */
            public static final String OPEN_MESSAGE_PAGE = BASE + "/message_page";
            /**
             * 跳转外部API 公积金
             */
            public static final String OPEN_NOTVERIFY_AF_INFO = BASE + "/open_api_notverify_af_info";
            /**
             * 用户强制登录
             */
            public static final String USER_FORCE_LOGIN = BASE + "/user_force_Login";
        }


        /**
         * 主页
         */
        public static final class Main {
            private static final String BASE = "/main";
            /**
             * 返回首页
             */
            public static final String HOME = BASE + "/home";
            /**
             * 借款界面
             */
            public static final String LEND = BASE + "/lend";
            /**
             * 还款界面
             */
            public static final String REPAY = BASE + "/repay";
            /**
             * 跳转发现
             */
            public static final String FIND = BASE + "/find";
            /**
             * 跳转更多其他
             */
            public static final String OTHER = BASE + "/other";
            /**
             * 我的
             */
            public static final String MY_CENTER = BASE + "/my_center";
            /**
             * 刷新tabBar
             */
            public static final String REFRESH_TABLIST = BASE + "/refresh_tablist";
        }


        /**
         * 认证功能：包括认证中心与各个认证项
         */
        public static final class Certifice {
            private static final String BASE = "/certifice";

            /**
             * 认证页面:完善资料-认证中心
             */
            public static final String CC = BASE + "/auth_center";
            /**
             * 流程认证中心
             */
            public static final String FLOW_PATH_CC = BASE + "/flow_path_cc";


            //************** 基础认证 **************

            /**
             * 个人信息认证
             */
            public static final String PERSION = BASE + "/persion";
            /**
             * 新个人信息认证
             */
            public static final String NEW_PERSION = BASE + "/new_persion";
            /**
             * 人脸、身份证识别
             */
            public static final String FACE_IDCARD = BASE + "/face_idcard";
            /**
             * 紧急联系人认证
             */
            public static final String CONTACT = BASE + "/contact";
            public static final String OPENAPI_CONTACT = BASE + "/openapi/contact";


            /**
             * 外部API_个人信息认证
             */
            public static final String OPEN_API_PERSION = BASE + "/open_api_persion";

            public static final class Phone {
                private static final String BASE = Certifice.BASE + "/phone";

                /**
                 * 手机运营商认证
                 */
                public static final String OPERATPR = BASE + "/operator";
                /**
                 * 手机运营商认证:需要参数，进行数据验证
                 * 需要先认证个人信息
                 */
                public static final String OPERATOR_AFTER_VERIFY = BASE + "/operator/verify";
            }

            public static final class Bank {
                private static final String BASE = Certifice.BASE + "/bank";

                /**
                 * 银行信息认证
                 */
                public static final String ADD = BASE + "/add";
                /**
                 * 银行信息认证:需要参数，进行数据验证
                 * 没有完善银行卡信息，则添加银行卡；否则跳转到银行卡详情页面
                 */
                public static final String ADD_AFTER_VERIFY = BASE + "/add/verify";
            }

            //************** 现在没有使用的认证 **************

            public static final class Gxbao {
                private static final String BASE = Certifice.BASE + "/gxbao";

                /**
                 * 公信宝：直接进入公信宝的h5页面
                 */
                public static final String GXBAO = BASE + "/";
                /**
                 * 公信宝认证:需要参数，进行数据验证
                 * 向后台请求公信宝页面地址，若错误则弹提示
                 */
                public static final String GXBAO_AFTER_VERIFY = BASE + "/verify";
            }

            public static final class Zmxy {
                private static final String BASE = Certifice.BASE + "/zmxy";

                /**
                 * 芝麻信用认证
                 */
                public static final String ZMXY = BASE + "/";
                /**
                 * 芝麻信用认证:需要参数，进行数据验证
                 */
                public static final String ZMXY_AFTER_VERIFY = BASE + "/verify";
            }


            //************** 附加认证 **************

            public static final class Work {
                private static final String BASE = Certifice.BASE + "/work";

                /**
                 * 工作信息认证
                 */
                public static final String WORK = BASE + "/";
                /**
                 * 工作信息认证:需要参数，进行数据验证
                 * 需要先认证个人信息，之后才能认证工作信息
                 */
                public static final String WORK_AFTER_VERIFY = BASE + "/verify";
            }

            public static final class Accumulationfund {
                private static final String BASE = Certifice.BASE + "/accumulationfund";

                /**
                 * 公积金认证
                 */
                public static final String AUTH = BASE + "/";
                /**
                 * 公积金认证（大额分期认证，需要先认证基础认证信息和工作信息）
                 */
                public static final String AUTH_VERIFY = BASE + "/verify";
                /**
                 * 公积金认证(28天分期认证，需要先认证个人信息和工作信息)
                 */
                public static final String AUTH_28DAY_VERIFY = BASE + "/28day/verify";
            }


            //************** 加分认证 **************

            public static final class Social {
                private static final String BASE = Certifice.BASE + "/social";

                /**
                 * 社保认证
                 */
                public static final String SOCIAL = BASE + "/";
                /**
                 * 社保卡认证:需要参数，进行数据验证
                 */
                public static final String SOCIAL_AFTER_VERIFY = BASE + "/verify";
            }

            public static final class Moxie {
                private static final String BASE = Certifice.BASE + "/moxie";

                /**
                 * 魔蝎认证：对魔蝎SDK的参数进行了包装、转换
                 * 推荐使用该指令
                 */
                public static final String MOXIE = BASE + "/";

                /**
                 * 车险认证
                 */
                public static final String INSURANCE = BASE + "/insurance";
                /**
                 * 保单认证
                 */
                public static final String LIFEINSURANCE = BASE + "/lifeinsurance";
                /**
                 * 邮箱账单认证
                 */
                public static final String EMAILBILLS = BASE + "/emailBills";
                /**
                 * 邮箱账单认证：指令中需要有参数，进行验证
                 */
                public static final String EMAILBILLS_NEED_VERIFY = BASE + "/emailbills/verify";
                /**
                 * 支付宝认证
                 */
                public static final String ALIPAY = BASE + "/alipay";
                /**
                 * 支付宝认证（魔蝎）：指令中需要有参数，进行验证
                 */
                public static final String ALIPAY_NEED_VERIFY = BASE + "/alipay/verify";
            }

            /**
             * 学信网认证
             */
            public static final String SCHOOLROLL = BASE + "/schoolroll";

            /**
             * 其他认证(更多、网页)
             */
            public static final String OTHER = BASE + "/other";


            //************** 没有使用过的认证 **************

            /**
             * 卡片等级
             */
            public static final String CARD_LEVEL = BASE + "/card_level";
            /**
             * 工资卡认证
             */
            public static final String SALARY = BASE + "/salary";
        }


        /**
         * 网页
         */
        public static final class H5 {
            private static final String BASE = "/h5";
            /**
             * 跳转到APP内部的网页界面，打开URL：使用原生的webview
             */
            public static final String URL = BASE + "/url";

            private static final String FACE_OCR = H5.BASE + "/faceOcr";

            /**
             * 腾讯的X5内核
             */
            public static final class X5 {
                private static final String BASE = H5.BASE + "/x5";
                /**
                 * 横屏：使用的是腾讯的X5内核
                 */
                public static final String HORIZONTAL = BASE + "/horizontal";
            }

            /**
             * APP内部webview的功能
             */
            public static final class Webview {
                private static final String BASE = H5.BASE + "/webview";
                /**
                 * 显示红包，白条老逻辑
                 */
                public static final String BENEFIT = BASE + "/benefit";
                /**
                 * 上传还款凭证
                 */
                public static final String UPLOAD_REPAY_VOUCHER = BASE + "/upload/repay_voucher";
                /**
                 * webview页面中，右上角的点击功能
                 */
                public static final String TOP_RIGHT_BUTTON = BASE + "/titlebar/right_button";
                /**
                 * webview页面中，左上角的点击功能
                 */
                public static final String TOP_LEFT_BUTTON = BASE + "/titlebar/left_button";
                /**
                 * 透明的webview
                 */
                public static final String TRANSPARENT_WEBVIEW = BASE + "/transparent_webview";
            }
        }


        /**
         * 手机功能：不属于APP内部操作
         */
        public static final class App {
            private static final String BASE = "/app";
            /**
             * 返回上一个页面(针对Activity，不是fragment)
             * 同时对webview page有特殊处理
             */
            public static final String BACK = BASE + "/common_black_back_icon";
            /**
             * 跳转QQ客服
             */
            public static final String QQ = BASE + "/qq";
            /**
             * 唤醒微信
             */
            public static final String OPEN_WECHAT = BASE + "/wechat";
            /**
             * 打开浏览器
             */
            public static final String OPEN_BROWSER = BASE + "/browser";
            /**
             * 弹出拨打电话的dialog
             */
            public static final String SHOW_CALL_PHONE = BASE + "/phone/call/dialog";
            /**
             * 分享
             */
            public static final String SHARE = BASE + "/share";
            /**
             * weex跳转
             */
            public static final String WEEX = BASE + "/weex";
            public static final String WEEX_PICK_IMAGE = BASE + "/weex/pickimage";

            /*
             * 跳转小程序
             */
            public static final String GOTO_WECHAT_MINI = BASE + "/goto_wechat_mini";

            public static final String CUSTOMER_SERVICE_CENTER = BASE + "/customer_service";

        }


        /**
         * 第三方数据或APP数据
         */
        public static final class Data {
            private static final String BASE = "/data";
            /**
             * 操作日志上报
             */
            public static final String OPERATION_LOG = BASE + "/operation_log";
            /**
             * 百融
             */
            public static final String BAI_RONG = BASE + "/bairong";

            public static final String GET_CONTACT_FROM_APP = BASE + "/get_contact_from_app";
        }


        /**
         * UI
         */
        public static final class Ui {
            private static final String BASE = "/ui";

            /**
             * UI提示
             */
            public static final class Tip {
                private static final String BASE = Ui.BASE + "/tip";
                /**
                 * Toast提示
                 */
                public static final String TOAST = BASE + "/toast";
                /**
                 * Dialog提示
                 */
                public static final String DIALOG = BASE + "/dialog";

                /**
                 * 推送显示对话框
                 */
                public static final String PUSH_DIALOG = BASE + "/push/dialog";

                /**
                 * 推送显示对话框
                 */

                public static final String PUSH_NOTIFICATION = BASE + "/push/Notification";
            }
        }

        /**
         * 埋点
         */
        public static final class GrowingIO {
            private static final String BASE = "/growingio";

            //设置页面名称
            private static final String PAGENAME = BASE + "/page_name";

            //埋点
            private static final String BURIED_POINT = BASE + "/buried_point";
            //GrowingIO埋点上传后台
            private static final String BI = BASE + "/bi";
        }

        /**
         * 支付
         */
        public static final class Pay {
            private static final String BASE = "/pay";

            private static final String WX_PAY = BASE + "/wxpay";
            private static final String ALI_PAY = BASE + "/alipay";
        }

        /**
         * 购物车
         */
        public static final class ShoppingCart {
            private static final String BASE = "/shoppingcart";

            //购物车操作
            private static final String OPERATE = BASE + "/operate";

            //下单
            private static final String ORDERS = BASE + "/orders";

            //购物车
            private static final String WINDOW = BASE + "/window";
        }

    }


}
