package common.repository.http.entity.home;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class MenuItemBean implements Serializable {
    private int id;
    private String val;
    private String icon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
