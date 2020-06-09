package common.events;

public class WeChatPayEvent {
    private int type;//0成功  1失败 2取消

    public WeChatPayEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
