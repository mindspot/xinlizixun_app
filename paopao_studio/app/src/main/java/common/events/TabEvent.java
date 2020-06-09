package common.events;

/**
 * Created by hpzhan on 2020/2/18.
 */

public class TabEvent {

    public static final int TYPE_CHANGE_TAB = 1;
    public static final int TYPE_UPLOAD_TAB_MSG_NUM = 2;

    private int tabId;
    private int type;
    private int msgNum;

    public TabEvent(int type, int tabId) {
        this.tabId = tabId;
        this.type = type;
    }

    public TabEvent(int type, int tabId, int num) {
        this.tabId = tabId;
        this.type = type;
        this.msgNum = num;
    }

    public int getTabId() {
        return tabId;
    }

    public void setTabId(int tabId) {
        this.tabId = tabId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getMsgNum() {
        return msgNum;
    }

    public void setMsgNum(int msgNum) {
        this.msgNum = msgNum;
    }
}
