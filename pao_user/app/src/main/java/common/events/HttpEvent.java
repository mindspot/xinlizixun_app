package common.events;

/**
 * Created by Administrator on 2017/12/14.
 */
public class HttpEvent {

    /**
     * 判断是否要重新登录
     */
    public static final int HTTP_ERROR_NEED_LOGIN = -2;
    /**
     * 判断用户是否需要输入验证码
     */
    public static final int HTTP_ERROR_REQUEST_CHECK_VERIFICATION_CODE = -3;

    /**
     * token过期
     */
    public static final int HTTP_ERROR_NEED_GET_TOKEN = -4;

    private final int code;

    public HttpEvent(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
