package common.repository.http.entity.base;

/**
 * Created by User on 2018/1/26.
 */

public class OperateDialogBean {
    /**
     * 标题
     */
    private String title;
    /**
     * 内容
     */
    private String content;
    /**
     * 确认按钮文案
     */
    private String surebtn_text;
    /**
     * 取消按钮文案
     */
    private String cancelbtn_text;
    /**
     * 跳转
     */
    private String jump;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSurebtn_text() {
        return surebtn_text;
    }

    public void setSurebtn_text(String surebtn_text) {
        this.surebtn_text = surebtn_text;
    }

    public String getCancelbtn_text() {
        return cancelbtn_text;
    }

    public void setCancelbtn_text(String cancelbtn_text) {
        this.cancelbtn_text = cancelbtn_text;
    }

    public String getJump() {
        return jump;
    }

    public void setJump(String jump) {
        this.jump = jump;
    }
}
