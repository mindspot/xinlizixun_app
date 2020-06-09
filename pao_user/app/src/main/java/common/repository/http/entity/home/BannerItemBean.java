package common.repository.http.entity.home;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/17.
 */

public class BannerItemBean implements Serializable {
    private int id;
    private String val;
    private String link;

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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
