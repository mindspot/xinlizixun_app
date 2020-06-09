package com.framework.http;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.framework.http.bean.HttpError;
import com.framework.http.bean.RequestBean;
import com.framework.http.interfaces.HttpInterface;
import com.framework.http.interfaces.HttpResultInterface;
import com.framework.utils.ConvertUtil;
import com.framework.utils.LogUtil;
import com.socks.library.KLog;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/3/21.
 */
public class OkHttpWrapper implements HttpInterface {

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");


    private final OkHttpClient mOkHttpClient;
    private final String userAgent;
    private final HttpAnalyzer analyzer;


    public OkHttpWrapper(Context context, boolean printLog) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS);
        mOkHttpClient = builder.build();
        this.userAgent = UserAgent.get();
        HttpLogUtil.setLog(printLog);
        this.analyzer = new HttpAnalyzer(printLog);
    }


    @Override
    public void get(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        String query = createQuery(url, requestBean);
        requestBuilder.url(url + query).get();
        enqueue(url, requestBuilder, callback);
    }

    @Override
    public void delete(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        RequestBody body = createFormBody(requestBean);
        requestBuilder.url(url).delete(body);
        enqueue(url, requestBuilder, callback);
    }


    @Override
    public void put(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        String body = createJsonFormBody(requestBean);
        requestBuilder.url(url).put(RequestBody.create(MEDIA_TYPE_JSON, body));
        enqueue(url, requestBuilder, callback);
    }

    @Override
    public void post(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        RequestBody body = createFormBody(requestBean);
        requestBuilder.url(url).post(body);
        enqueue(url, requestBuilder, callback);
    }


    @Override
    public void postJSON(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        String body = createJsonFormBody(requestBean);
        requestBuilder.url(url).post(RequestBody.create(MEDIA_TYPE_JSON, body));
        enqueue(url, requestBuilder, callback);
    }

    @Override
    public void postJSONBody(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        requestBuilder.url(url).post(RequestBody.create(MEDIA_TYPE_JSON, requestBean.getBodyStr()));
        enqueue(url, requestBuilder, callback);
    }

    @Override
    public void getSync(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        String query = createQuery(url, requestBean);
        requestBuilder.url(url + query).get();
        execute(url, requestBuilder, callback);
    }

    @Override
    public void postSync(Object tag, String url, RequestBean requestBean, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        RequestBody body = createFormBody(requestBean);
        requestBuilder.url(url).post(body);
        execute(url, requestBuilder, callback);
    }

    public void upload(Object tag, String url, Map<String, String> headers, String uploadKey, File file, Map<String, String> formDataPartMap,
                       HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, headers);

        //构造上传请求，类似web表单
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder();
        bodyBuilder.setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart(uploadKey, file.getName(), RequestBody.create(MEDIA_TYPE_JSON, file));
        if (formDataPartMap != null && !formDataPartMap.isEmpty()) {
            for (Map.Entry<String, String> entry : formDataPartMap.entrySet()) {
                bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        //进行包装，使其支持进度回调
        requestBuilder.url(url).post(bodyBuilder.build());
        enqueue(url, requestBuilder, callback);
    }


    /**
     * 文件下载
     */
    public void download(Object tag, String url, RequestBean requestBean, final String filePath, HttpResultInterface<String> callback) {
        Request.Builder requestBuilder = new Request.Builder().tag(tag);
        addHeader(requestBuilder, requestBean.getHeaders());
        String query = createQuery(url, requestBean);
        requestBuilder.url(url + query).get();
        enqueueDownload(requestBuilder, filePath, callback);
    }


    //************************* header *************************
    private static void addHeader(Request.Builder requestBuilder, Map<String, String> headers) {
        if (headers == null || headers.isEmpty()) {
            return;
        }
        for (Map.Entry<String, String> header : headers.entrySet()) {
            try {
                requestBuilder.addHeader(header.getKey(), header.getValue());
            } catch (IllegalArgumentException | NullPointerException e) {
                KLog.w("http header key is error...", headers, e);
                CrashReport.postCatchedException(new Throwable("request header error:"
                        + "\n--> key:" + header.getKey() + ", value:" + header.getValue()
                        + "\n--> headers:" + headers.toString(), e));
            }
        }
    }


    //************************* param *************************
    private static String createQuery(String url, RequestBean requestBean) {
        Map<String, String> params = getParams(requestBean);
        if (params == null || params.isEmpty()) {
            return "";
        }


        StringBuilder queryBuilder = new StringBuilder();
        if (url.contains("?")) {
            queryBuilder.append("&");
        } else {
            queryBuilder.append("?");
        }

        for (Map.Entry<String, String> entry : params.entrySet()) {
            queryBuilder.append(entry.getKey()).append("=");
            try {
                queryBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            queryBuilder.append("&");
        }
        queryBuilder.deleteCharAt(queryBuilder.length() - 1);
        return queryBuilder.toString();
    }

    private static RequestBody createFormBody(RequestBean requestBean) {
        Map<String, String> params = getParams(requestBean);
        if (params == null || params.isEmpty()) {
            return new FormBody.Builder().build();
        }

        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            formBodyBuilder.add(entry.getKey(), entry.getValue());
        }
        return formBodyBuilder.build();
    }

    private static String createJsonFormBody(RequestBean requestBean) {
        String result = "";
        Map<String, String> params = getParams(requestBean);
        if (params == null || params.isEmpty()) {
            return result;
        }
        JSONObject jsonObject = new JSONObject();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                Object obj = JSON.parse(entry.getValue().toString());
                if (obj instanceof JSONObject) {
                    jsonObject.put(entry.getKey(), JSONObject.parseObject(entry.getValue()));
                } else if (obj instanceof JSONArray) {
                    jsonObject.put(entry.getKey(), JSONArray.parseArray(entry.getValue()));
                } else {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            } catch (Exception ex) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        }
        result = jsonObject.toJSONString();
        return result;
    }

    private static Map<String, String> getParams(RequestBean requestBean) {
        Map<String, String> params = requestBean.getParams();
        if (params == null) {
            params = ConvertUtil.getMyParams(requestBean);
        }
        return params;
    }


    @NonNull
    private Call enqueue(String originUrl, final Request.Builder requestBuilder, final HttpResultInterface<String> callback) {
        final String lineTag = HttpLogUtil.createRequestLineTag(7);

        requestBuilder.header(UserAgent.KEY, userAgent);
        Request request = requestBuilder.build();
        HttpLogUtil.printRequestLog(lineTag, request);
        analyzer.analysis(originUrl, request, lineTag);

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {// request failure
                e.printStackTrace();
                final String url = call.request().url().toString();
                HttpLogUtil.printResponseErrorLog(lineTag, url, e);
                if (callback == null) {
                    return;
                }
                callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final int responseCode = response.code();
                final String responseMessage = response.message();
                final String url = call.request().url().toString();

                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    String responseData = body == null ? null : body.string();
                    HttpLogUtil.printResponseLog(lineTag, responseCode, responseMessage, responseData, url);
                    if (callback != null)
                        callback.onSuccess(responseData);
                } else {// request success, response failure
                    HttpLogUtil.printResponseLog(lineTag, responseCode, responseMessage, response.body() == null ? null : response.body().string(), url);
                    if (callback != null)
                        callback.onFailed(new HttpError(responseCode, responseMessage));
                }
            }
        });
        return call;
    }

    @NonNull
    private Call execute(String originUrl, final Request.Builder requestBuilder, final HttpResultInterface<String> callback) {
        final String lineTag = HttpLogUtil.createRequestLineTag(7);

        requestBuilder.header(UserAgent.KEY, userAgent);
        Request request = requestBuilder.build();
        HttpLogUtil.printRequestLog(lineTag, request);
        analyzer.analysis(originUrl, request, lineTag);

        Call call = mOkHttpClient.newCall(request);

        final String url = call.request().url().toString();

        try {
            Response response = call.execute();
            final int responseCode = response.code();
            final String responseMessage = response.message();
            final String result = response.body().string();
            HttpLogUtil.printResponseLog(lineTag, responseCode, responseMessage, result, url);
            callback.onSuccess(result);
        } catch (IOException e) {
            e.printStackTrace();
            HttpLogUtil.printResponseErrorLog(lineTag, url, e);
            HttpError error = new HttpError();
            error.setErrMessage(e.getMessage());
            callback.onFailed(error);
        }
        return call;
    }

    public void sendMultipart(String url) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("title", "wangshu")
                .addFormDataPart("image", "wangshu.jpg",
                        RequestBody.create(MEDIA_TYPE_PNG, new File("/sdcard/wangshu.jpg")))
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + "...").url(url).post(requestBody)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("wangshu", response.body().string());
            }
        });
    }


    @NonNull
    private Call enqueueDownload(final Request.Builder requestBuilder, final String filePath, final HttpResultInterface<String> callback) {
        final String lineTag = HttpLogUtil.createRequestLineTag(7);

        requestBuilder.header(UserAgent.KEY, userAgent);
        Request request = requestBuilder.build();
        HttpLogUtil.printRequestLog(lineTag, request);
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {// request failure
                e.printStackTrace();
                final String url = call.request().url().toString();
                HttpLogUtil.printResponseErrorLog(lineTag, url, e);

                File downloadedFile = new File(filePath);
                if (downloadedFile.exists()) {
                    downloadedFile.delete();
                }

                if (callback == null) {
                    return;
                }
                callback.onFailed(new HttpError(HttpError.HTTP_ERROR_CONNECT, HttpError.HTTP_ERROR_CONNECT_MSG));
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                final int responseCode = response.code();
                final String responseMessage = response.message();
                final String url = call.request().url().toString();

                if (response.isSuccessful() && handleDownload(response)) {
                    HttpLogUtil.printResponseLog(lineTag, responseCode, responseMessage, "下载成功, file store path:" + filePath, url);
                    if (callback != null) {
                        callback.onSuccess("下载成功");
                    }
                } else {// request success, response failure
                    HttpLogUtil.printResponseLog(lineTag, responseCode, responseMessage, null, url);
                    if (callback != null) {
                        callback.onFailed(new HttpError(responseCode, responseMessage));
                    }
                }
            }

            private boolean handleDownload(Response response) {
                ResponseBody body = response.body();
                long filesize = body.contentLength();
                InputStream is = body.byteStream();
                if (is == null || filesize == 0) {
                    LogUtil.Log("tag", "网络文件不存在");
                    return false;
                }

                LogUtil.Log("tag", "网络文件存在, filePath:" + filePath);

                OutputStream os = null;//context.openFileOutput(file.getPath(), Context.MODE_PRIVATE);
                try {
                    File temp = new File(filePath + ".downloading");
                    if (temp.exists()) {
                        if (!temp.delete()) {
                            return false;
                        }
                    }
                    os = new FileOutputStream(temp);
                    byte[] buff = new byte[1024];
                    int len;
                    while ((len = is.read(buff)) > 0) {
                        os.write(buff, 0, len);
                    }

                    File downloadedFile = new File(filePath);

                    if (downloadedFile.exists()) {
                        if (!downloadedFile.delete()) {
                            return false;
                        }
                    }
                    return temp.renameTo(downloadedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return false;
            }
        });
        return call;
    }


    @Override
    public void cancelRequest(Object tag) {
        analyzer.delete();
        List<Call> queuedCalls = mOkHttpClient.dispatcher().queuedCalls();
        for (Call call : queuedCalls) {
            cancel(tag, call);
        }
        List<Call> runningCalls = mOkHttpClient.dispatcher().runningCalls();
        for (Call call : runningCalls) {
            cancel(tag, call);
        }
    }

    private void cancel(Object tag, Call call) {
        Object requestTag = call.request().tag();
        if (requestTag == null || tag == null) {
            return;
        }
        if (requestTag.equals(tag))
            call.cancel();
    }

}
