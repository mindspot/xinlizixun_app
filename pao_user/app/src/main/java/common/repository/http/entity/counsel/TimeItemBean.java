package common.repository.http.entity.counsel;

import java.io.Serializable;

/**
 * Created by hpzhan on 2020/2/21.
 */

public class TimeItemBean implements Serializable {
    private String val;
    private int valCode;
    private int id;
    private boolean isCheck;

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public int getValCode() {
        return valCode;
    }

    public void setValCode(int valCode) {
        this.valCode = valCode;
    }
}
