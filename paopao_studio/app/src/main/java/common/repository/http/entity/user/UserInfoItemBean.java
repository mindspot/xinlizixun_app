package common.repository.http.entity.user;


import common.repository.http.param.BaseResponseBean;

/**
 * Created by User on 2017/3/30.
 */

public class UserInfoItemBean extends BaseResponseBean {
    private UserInfoBean item;

    public UserInfoBean getItem() {
        return item;
    }

    public void setItem(UserInfoBean item) {
        this.item = item;
    }
}
