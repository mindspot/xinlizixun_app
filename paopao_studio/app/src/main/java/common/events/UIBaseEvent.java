package common.events;

public class UIBaseEvent {
    public final static int EVENT_DEFAULT = -1;//默认
    public final static int EVENT_LOGIN = 0;//登陆
    public final static int EVENT_LOGOUT = 1;//退出
    public final static int EVENT_LOAN_SUCCESS = 2;//申请成功
    public final static int EVENT_LOAN_FAILED = 3;//申请失败
    public final static int EVENT_SET_PAYPWD = 4;//设置交易密码
    public final static int EVENT_SET_PWD = 5;//设置登录密码
    public final static int EVENT_GOTO_LIFTQUOTA = 6;
    /**
     * 登入登出底部tab请求
     */
    public final static int EVENT_UPLOAD_MAIN_TAB_LIST = 7;

    /**
     * 更新购物车数据
     */
    public final static int EVENT_UPLOAD_SHOPPING_CART_DATA = 9;
    /**
     * 更新收藏
     */
    public final static int EVENT_UPLOAD_COLLECTION_DATA = 10;
    /**
     * 更新浏览数据
     */
    public final static int EVENT_UPLOAD_BROWSE_DATA = 11;
    /**
     * 登录态失效点击取消
     */
    public final static int EVENT_CODE_FAIL_TAB_BAR_ONE = 12;
    /**
     * 更新 人脸 身份证识别页
     */
    public final static int EVENT_UPLOAD_FACE_IDCARD_DISCERN_DATA = 13;

    //聊天选择图片
    public final static int EVENT_CHAT_SELECT_IMAGE = 14;
    public final static int EVENT_CHAT_SELECT_CAMCRE = 15;

    private String code;
    private String message;

    public UIBaseEvent() {
    }

    public UIBaseEvent(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
