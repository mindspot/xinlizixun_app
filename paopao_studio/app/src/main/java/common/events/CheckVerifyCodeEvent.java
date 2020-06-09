package common.events;

/**
 * Created by Administrator on 2017/9/4.
 * 校验验证码提示框事件
 */
public class CheckVerifyCodeEvent {
    private final boolean success;// 校验成功与否

    public CheckVerifyCodeEvent(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

}
