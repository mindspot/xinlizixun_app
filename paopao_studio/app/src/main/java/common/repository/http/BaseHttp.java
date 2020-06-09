package common.repository.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.URLUtil;

import com.alibaba.fastjson.JSONObject;
import com.framework.http.OkHttpWrapper;
import com.framework.http.bean.HttpError;
import com.framework.http.bean.RequestBean;
import com.framework.http.interfaces.HttpResultInterface;
import com.framework.page.Page;
import com.framework.utils.ConvertUtil;
import com.framework.utils.LogUtil;
import com.framework.utils.StringUtil;
import com.framework.utils.ViewUtil;
import com.socks.library.KLog;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import base.UserCenter;
import common.events.HttpEvent;
import common.repository.http.param.BaseRequestBean;
import common.repository.http.param.BaseResponseBean;
import common.repository.http.param.FileBean;
import common.repository.http.param.RequestHeaderHelper;
import common.repository.share_preference.SPApi;
import common.router.CommandRequest;
import de.greenrobot.event.EventBus;

public class BaseHttp {
    public static final int HTTP_REQUEST_TYPE_POST = 1;
    public static final int HTTP_REQUEST_TYPE_POSTJSON = 2;
    public static final int HTTP_REQUEST_TYPE_GET = 3;
    public static final int HTTP_REQUEST_TYPE_DELETE = 4;
    public static final int HTTP_REQUEST_TYPE_PUT = 5;

    public static final String HTTP_ERROR_REQUEST = "0";//服务接口正常
    public static final String HTTP_ERROR_NEED_TOKEN = "-9999";//需要更新token
    public static final String HTTP_ERROR_NEED_LOGIN = "-9998";//判断是否要重新登录
    public static final String HTTP_ERROR_RESTRICT_LOGIN = "-9997";//限制登入
    public static final String HTTP_ERROR_REQUEST_CHECK_VERIFICATION_CODE = "-3";//判断用户是否需要输入验证码
    public static final String HTTP_ERROR_REQUEST_OTHER = "1001";//服务接口做其他特殊处理
    private final OkHttpWrapper httpWrapper;
    private Handler handler;
    private Context context;

    public BaseHttp(Context context, boolean printLog) {
        this.context = context;
        handler = new Handler(Looper.getMainLooper());
        this.httpWrapper = new OkHttpWrapper(context, printLog);
    }

    public String getUrl(String url) {
        LogUtil.Log("=======", url);
        if (StringUtil.isBlank(url)) {
            return "";
        }
        String ret_url = "";
        if (url.contains("clientType=android&appVersion=")) {
            return url;
        } else {
            if (url.contains("?")) {
                ret_url = url + "&";
            } else {
                ret_url = url + "?";
            }

            int i = 0;
            for (Map.Entry<String, String> header : RequestHeaderHelper.getHeaders().entrySet()) {
                if (i == 0) {
                    ret_url += header.getKey() + "=" + header.getValue();
                } else {
                    ret_url += "&" + header.getKey() + "=" + header.getValue();
                }
                i++;
            }
            return ret_url.replace(" ", "");
        }
    }

    private <T> void onImageResultSuccess(Page page, String result, HttpCallback<T> callback) {
        if (callback == null) {
            return;
        }
        if (result == null) {
            callback.onFailed(new HttpError(HttpError.HTTP_ERROR_REQUEST, ""));
            return;
        }

        try {
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.containsKey("url") && !StringUtil.isBlank(jsonObject.getString("path"))) {
                callback.onSuccess(Integer.parseInt(HTTP_ERROR_REQUEST), "上传成功!", (T) jsonObject.getString("path"));
            } else {
                HttpError error = new HttpError(Integer.parseInt(HTTP_ERROR_REQUEST_OTHER), "图片上传失败！");
                callback.onFailed(error);
            }
        } catch (Exception ex) {
        }
    }

    private <T> void onResultSuccess(Page page, int rqtType, String url, RequestBean rqtBean, String result, HttpCallback<T> callback) {
        if (callback == null) {
            return;
        }
        if (result == null) {
            callback.onFailed(new HttpError(HttpError.HTTP_ERROR_REQUEST, ""));
            return;
        }

        try {
            BaseResponseBean bean = ConvertUtil.toObject(result, BaseResponseBean.class);
            String code = bean.getCode();
            hanldePushData(page, bean.getExtra_command());

            if (HTTP_ERROR_NEED_LOGIN.equals(code) || HTTP_ERROR_RESTRICT_LOGIN.equals(code)) {
                HttpError error = new HttpError(Integer.parseInt(bean.getCode()), bean.getMessage());
                callback.onFailed(error);
                UserCenter.instance().logout(page);
                UserCenter.instance().toLogin(page);
                return;
            }
            if (HTTP_ERROR_NEED_TOKEN.equals(code)) {
                LogUtil.Log("token", "token 过期：" + url);
                SPApi.user().setSessionId(bean.getResult());
                switch (rqtType) {
                    case HTTP_REQUEST_TYPE_POST:
                        post(page, url, rqtBean, callback);
                        break;
                    case HTTP_REQUEST_TYPE_POSTJSON:
                        postJSON(page, url, rqtBean, callback);
                        break;
                    case HTTP_REQUEST_TYPE_GET:
                        get(page, url, rqtBean, callback);
                        break;
                    case HTTP_REQUEST_TYPE_DELETE:
                        delete(page, url, rqtBean, callback);
                        break;
                    case HTTP_REQUEST_TYPE_PUT:
                        put(page, url, rqtBean, callback);
                        break;
                }
                return;
            }
            if (HTTP_ERROR_REQUEST_CHECK_VERIFICATION_CODE.equals(code)) {
                HttpError error = new HttpError(Integer.parseInt(bean.getCode()), "");
                callback.onFailed(error);
                EventBus.getDefault().post(new HttpEvent(HttpEvent.HTTP_ERROR_REQUEST_CHECK_VERIFICATION_CODE));
                return;
            }
            if (code.equals(HTTP_ERROR_REQUEST) || code.equals(HTTP_ERROR_REQUEST_OTHER)) {
                Type[] types = callback.getClass().getGenericInterfaces();
                Type type;
                if (types == null || types.length == 0) {
                    type = callback.getClass().getGenericSuperclass();
                } else {
                    type = types[0];
                }
                ParameterizedType paramType = (ParameterizedType) type;
                type = paramType.getActualTypeArguments()[0];

                T data;
                if ("class java.lang.String".equals(type.toString())) {
                    data = (T) bean.getResult();
                } else {
                    data = ConvertUtil.toObject(bean.getResult(), type);
                }
                callback.onSuccess(Integer.parseInt(code), bean.getMessage(), data);
            } else {
                HttpError error = new HttpError();
                error.setErrCode(Integer.parseInt(bean.getCode()));
                error.setErrMessage(bean.getMessage());
                error.setDetailMessage(bean.getResult());
                callback.onFailed(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("onResultSuccess", result);
            callback.onFailed(new HttpError(HttpError.HTTP_ERROR_DEFAULT, e.getMessage()));
        }
    }


    private void onResultFailed(HttpError error, HttpCallback callback) {
        if (callback == null) {
            return;
        }
        if (error.getCache() != null && error.getCache().length() > 0) {
            BaseResponseBean bean = ConvertUtil.toObject(error.getCache(), BaseResponseBean.class);
            error.setCache(bean.getResult());
        }
        callback.onFailed(error);
    }

    /**
     * 处理模拟推送数据
     */
    private void hanldePushData(Page page, String extra_command) {
        if (!TextUtils.isEmpty(extra_command)) {
            new CommandRequest(extra_command).setPage(page).router();
        }
    }

    /**
     * post请求
     */
    public <T> void post(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
                    }
                });
            }
            return;
        }

        String finalUrl = url;
        httpWrapper.post(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultSuccess(page, HTTP_REQUEST_TYPE_POST, finalUrl, bean, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                if (canNotCallback(page)) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    /**
     * get请求
     */
    public <T> void get(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
                    }
                });
            }
            return;
        }

        String finalUrl = url;
        httpWrapper.get(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultSuccess(page, HTTP_REQUEST_TYPE_GET, finalUrl, bean, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    /**
     * delete请求
     */
    public <T> void delete(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
                    }
                });
            }
            return;
        }
        String finalUrl = url;
        httpWrapper.delete(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultSuccess(page, HTTP_REQUEST_TYPE_DELETE, finalUrl, bean, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                if (canNotCallback(page)) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    /**
     * put请求
     */
    public <T> void put(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
                    }
                });
            }
            return;
        }
        String finalUrl = url;
        httpWrapper.put(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultSuccess(page, HTTP_REQUEST_TYPE_PUT, finalUrl, bean, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                if (canNotCallback(page)) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    /**
     * 获取地址信息
     */
    public <T> void addressInfo(final Page page, final HttpCallback<T> callback) {
        String url = "http://pv.sohu.com/cityjson?ie=utf-8";
        httpWrapper.get(page, url, new BaseRequestBean(), new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        if (callback == null) {
                            return;
                        }
                        if (result == null) {
                            callback.onFailed(new HttpError(HttpError.HTTP_ERROR_REQUEST, ""));
                            return;
                        }
                        int start = result.indexOf("{");
                        int end = result.indexOf("}");
                        String json = result.substring(start, end + 1);
                        String addressInfo = "";
                        if (json != null) {
                            try {
                                JSONObject jsonObject = JSONObject.parseObject(json);
                                addressInfo = jsonObject.getString("cname");
                                Log.d("address", "地址信息:" + addressInfo);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        callback.onSuccess(0, "获取成功", (T) addressInfo);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                if (canNotCallback(page)) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    //******************* can callback *******************

    private boolean canNotCallback(Page page) {
        if (page == null) {
            // request tag is null, or sync request
            return false;
        }

        boolean canNotCallback = ViewUtil.isFinishedPage(page);
        if (canNotCallback) {
            KLog.w("page can`t callback:" + page);
        }
        return canNotCallback;
    }

    /**
     * 同步的post请求
     */
    public <T> void postSync(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        httpWrapper.postSync(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                //onResultSuccess(page, result, callback);
            }

            @Override
            public void onFailed(final HttpError error) {
                onResultFailed(error, callback);
            }
        });
    }

    /**
     * 同步的get请求
     */
    public <T> void getSync(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        httpWrapper.getSync(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                //onResultSuccess(page, result, callback);
            }

            @Override
            public void onFailed(final HttpError error) {
                onResultFailed(error, callback);
            }
        });
    }

    /**
     * post请求
     */
    public <T> void postJSON(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
                    }
                });
            }
            return;
        }
        String finalUrl = url;
        httpWrapper.postJSON(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultSuccess(page, HTTP_REQUEST_TYPE_POSTJSON, finalUrl, bean, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                if (canNotCallback(page)) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    /**
     * post请求
     */
    public <T> void postJSONBody(final Page page, String url, RequestBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
                    }
                });
            }
            return;
        }

        httpWrapper.postJSONBody(page, url, bean, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        //onResultSuccess(page, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                if (canNotCallback(page)) {
                    return;
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

    /********
     * 取消请求
     *
     * @param tag
     */
    public void cancelRequest(Object tag) {
        httpWrapper.cancelRequest(tag);
    }


    /************
     * 文件上传
     */
    public <T> void upload(final Page page, String url, @NonNull FileBean bean, final HttpCallback<T> callback) {
        url = getUrl(url);
        if (!URLUtil.isNetworkUrl(url)) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_REQUEST, "图片文件不存在"));
                    }
                });
            }
            return;
        }
        File file = new File(bean.getFileSrc());
        if (!file.exists()) {
            if (callback != null) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        callback.onFailed(new HttpError(HttpError.HTTP_ERROR_REQUEST, "图片文件不存在"));
                    }
                });
            }
            return;
        }
        httpWrapper.upload(page, url, bean.getHeaders(), bean.getUpLoadKey(), file, bean.getExtraParms(), new HttpResultInterface<String>() {
            @Override
            public void onSuccess(final String result) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onImageResultSuccess(page, result, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }


    public void download(final Page page, String url, final RequestBean requestBean, final String filePath, final HttpCallback<String> callback) {
        url = getUrl(url);
        httpWrapper.download(page, url, requestBean, filePath, new HttpResultInterface<String>() {
            @Override
            public void onSuccess(String result) {
                BaseResponseBean responseBean = new BaseResponseBean(HTTP_ERROR_REQUEST, result, result);
                final String virtualResult = ConvertUtil.toJsonString(responseBean);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        //onResultSuccess(page, virtualResult, callback);
                    }
                });
            }

            @Override
            public void onFailed(final HttpError error) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (canNotCallback(page)) {
                            return;
                        }
                        onResultFailed(error, callback);
                    }
                });
            }
        });
    }

}
