package common.repository.http;

import com.framework.http.bean.HttpError;

/**
 * Created by Administrator on 2017/04/11.
 */
public interface HttpCallback<T> {
    /**
     * @param code    状态码
     * @param message 提示信息
     * @param data    数据
     */
    void onSuccess(int code, String message, T data);

    void onFailed(HttpError error);
}
