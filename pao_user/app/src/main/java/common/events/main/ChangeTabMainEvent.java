package common.events.main;


import common.events.UIBaseEvent;

public class ChangeTabMainEvent extends UIBaseEvent {
    private int tab;

    public ChangeTabMainEvent(int tab) {
        this.tab = tab;
    }

    public ChangeTabMainEvent(int tab, String code, String message) {
        super(code, message);
        this.tab = tab;
    }

    public void setTab(int tab) {
        this.tab = tab;
    }

    public int getTab() {
        return tab;
    }
}

