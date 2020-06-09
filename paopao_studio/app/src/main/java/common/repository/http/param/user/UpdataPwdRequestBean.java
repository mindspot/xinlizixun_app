package common.repository.http.param.user;


import common.repository.http.param.BaseRequestBean;

/**
 * 作者：黑哥 on 2016/9/22 14:20
 */
public class UpdataPwdRequestBean extends BaseRequestBean {

    //旧密码
    private String old_pwd;
    //新密码
    private String new_pwd;

    public String getNew_pwd() {
        return new_pwd;
    }

    public void setNew_pwd(String new_pwd) {
        this.new_pwd = new_pwd;
    }

    public String getOld_pwd() {
        return old_pwd;
    }

    public void setOld_pwd(String old_pwd) {
        this.old_pwd = old_pwd;
    }
}
