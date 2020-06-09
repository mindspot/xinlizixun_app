package com.framework.http.interfaces;


import com.framework.http.bean.RequestBean;

public interface HttpInterface {

    void post(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    void postJSON(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    void get(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    void delete(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    void put(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    void postSync(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    void getSync(Object tag, String url, RequestBean requet, HttpResultInterface<String> callback);

    /**
     * 取消请求
     */
    void cancelRequest(Object tag);

}
