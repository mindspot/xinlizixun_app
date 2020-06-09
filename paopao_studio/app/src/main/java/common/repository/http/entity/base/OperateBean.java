package common.repository.http.entity.base;

/**
 * Created by User on 2018/1/26.
 * <p>
 * 交互 基类
 */

public class OperateBean {
    /**
     * 跳转的依赖，json格式字符串
     */
    private String jump;
    /**
     * 吐司提示
     */
    private String toast_tip;
    /**
     * dialog提示信息
     */
    private OperateDialogBean dialog;

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }

    public String getToast_tip() {
        return toast_tip;
    }

    public void setToast_tip(String toast_tip) {
        this.toast_tip = toast_tip;
    }

    public OperateDialogBean getDialog() {
        return dialog;
    }

    public void setDialog(OperateDialogBean dialog) {
        this.dialog = dialog;
    }
}
