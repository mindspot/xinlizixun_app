package com.framework.http.bean;

public class HttpError {

    public static final int HTTP_ERROR_SERVER = 20001;//服务器出错
    public static final String HTTP_ERROR_SERVER_MSG = "服务器出错";
    public static final int HTTP_ERROR_CONNECT = 20002;//连接出错
    public static final String HTTP_ERROR_CONNECT_MSG = "连接出错";
    public static final int HTTP_ERROR_REQUEST = 20003;//请求出错
    public static final String HTTP_ERROR_REQUEST_MSG = "请求出错";
    public static final int HTTP_ERROR_TIMEOUT = 20004;//连接超时
    public static final String HTTP_ERROR_TIMEOUT_MSG = "连接超时";
    public static final int HTTP_ERROR_URL = 20005;//网络地址错误
    public static final String HTTP_ERROR_URL_MSG = "网络地址错误";

    public static final int HTTP_ERROR_DEFAULT = 10001;
    public static final String HTTP_ERROR_DEFAULT_MSG = "未知错误";

    private int errCode;//错误编码
    private String errMessage;//错误信息
    private String cache;//缓存
    private String detailMessage;//详细错误信息

    public HttpError() {

    }

    public HttpError(int code, String errorMsg) {
        this.errCode = code;
        this.errMessage = errorMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public void setDetailMessage(String detailMessage) {
        this.detailMessage = detailMessage;
    }


}
