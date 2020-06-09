package common.events;

public class RefreshUIEvent extends UIBaseEvent {

    private int type = EVENT_DEFAULT;
    private int gotoLiftquota;

    public RefreshUIEvent(int type) {
        this.type = type;
    }

    public RefreshUIEvent(int type, String code, String message) {
        super(code, message);
        this.type = type;
    }

    public RefreshUIEvent(int type, int gotoLiftquota) {
        this.type = type;
        this.gotoLiftquota = gotoLiftquota;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public int getGotoLiftquota() {
        return gotoLiftquota;
    }

    public void setGotoLiftquota(int gotoLiftquota) {
        this.gotoLiftquota = gotoLiftquota;
    }
}

