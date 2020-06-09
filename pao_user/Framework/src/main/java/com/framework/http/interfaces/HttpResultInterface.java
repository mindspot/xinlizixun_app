package com.framework.http.interfaces;

import com.framework.http.bean.HttpError;

public interface HttpResultInterface<T> {

    /**
     * {code:0, message:xxx, data:bean}
     *
     * @param bean
     */
    void onSuccess(T bean);

    void onFailed(HttpError error);

}
