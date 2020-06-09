package common.repository.http.entity.counsel;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/21.
 */

public class QuestionItemBean implements Serializable {
    private String val;
    private String link;
    private String icon;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
