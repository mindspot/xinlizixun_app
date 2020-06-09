package common.events;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class ServiceWillStopEvent {
    private String msg;

    public ServiceWillStopEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
