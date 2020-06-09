package common.events;

public class PayResultEvent {
    private int type;//0成功  1失败 2取消

    public PayResultEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
