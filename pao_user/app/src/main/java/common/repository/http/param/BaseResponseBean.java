package common.repository.http.param;

import com.framework.http.bean.ResultBean;

public class BaseResponseBean extends ResultBean {

    private String code;
    private String result;
    private String message;
    private String extra_command;

    public BaseResponseBean() {
    }

    public BaseResponseBean(String code, String result, String message) {
        this.code = code;
        this.result = result;
        this.message = message;
    }

    public String getExtra_command() {
        return extra_command;
    }

    public void setExtra_command(String extra_command) {
        this.extra_command = extra_command;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
