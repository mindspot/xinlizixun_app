package com.framework.http;

import android.util.Log;

import com.socks.library.klog.JsonLog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.Okio;

/**
 * Created by Administrator on 2017/2/28.
 * print http log info
 */
public class HttpLogUtil {

    private static final String SUFFIX = ".java";

    private static boolean printLog = false;// 是否打印http日志

    public static void setLog(boolean log) {
        printLog = log;
    }

    public static String createRequestLineTag(int stackTraceIndex) {
        if (!printLog) {
            return "RequestLineTag";
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        StackTraceElement targetElement = stackTrace[stackTraceIndex];
        String className = targetElement.getClassName();
        String[] classNameInfo = className.split("\\.");
        if (classNameInfo.length > 0) {
            className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
        }

        if (className.contains("$")) {
            className = className.split("\\$")[0] + SUFFIX;
        }

        String methodName = targetElement.getMethodName();
        int lineNumber = targetElement.getLineNumber();

        if (lineNumber < 0) {
            lineNumber = 0;
        }
        return "[ (" + className + ":" + lineNumber + ")#" + methodName + " ] ";
    }


    public static void printRequestLog(String lineTag, okhttp3.Request request) {
        if (!printLog) {
            return;
        }
        String tag = "RequestLog";
        Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        Log.d(tag, "║ " + lineTag);
        Log.d(tag, "║ requestUrl:" + request.url());
        Log.d(tag, "║ method    :" + request.method());
        printHeaders(tag, request);
        printParams(tag, request);
        Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }

    private static void printHeaders(String tag, okhttp3.Request request) {
        Log.d(tag, "║═══════════════════headers═══════════════════");
        Headers headers = request.headers();
        for (String headerName : headers.names()) {
            List<String> headerValues = headers.values(headerName);
            Log.d(tag, "║ " + headerName + ":" + headerValues.toString());
        }
    }

    private static void printParams(String tag, okhttp3.Request request) {
        Log.d(tag, "║═══════════════════params═══════════════════");
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return;
        }

        if (requestBody instanceof FormBody) {
            printFormBody(tag, (FormBody) requestBody);
        } else if (requestBody instanceof MultipartBody) {
            printMultipartBody(tag, (MultipartBody) requestBody);
        }
    }

    private static void printFormBody(String tag, FormBody body) {
        Log.d(tag, "║ contentLength:" + body.contentLength());
        MediaType contentType = body.contentType();
        Log.d(tag, "║ contentType:" + contentType.toString() + ", type:" + contentType.type() + ", subtype:" + contentType.subtype() + ", charset:" + contentType.charset());
        for (int i = 0; i < body.size(); i++) {
            Log.d(tag, "║ " + body.name(i) + ":" + body.value(i));
        }
    }

    private static void printMultipartBody(String tag, MultipartBody body) {
        // 使用OKhttp提供的方法进行body的打印
//        try {
//            ByteArrayOutputStream socket = new ByteArrayOutputStream();
//            BufferedSink sink = Okio.buffer(Okio.sink(socket));
//            body.writeTo(sink);
//            sink.flush();
//            Log.d(tag, "║ MultipartBody\n" + socket.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // 将body中所有的part打印出来
        for (MultipartBody.Part part : body.parts()) {
            try {
                Field bodyField = MultipartBody.Part.class.getDeclaredField("headers");
                bodyField.setAccessible(true);
                Headers partHeaders = (Headers) bodyField.get(part);
                Log.d(tag, "║ " + partHeaders.toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            try {
                Field headersField = MultipartBody.Part.class.getDeclaredField("body");
                headersField.setAccessible(true);
                RequestBody partBody = (RequestBody) headersField.get(part);

                MediaType contentType = partBody.contentType();
                if (contentType != null) {
                    Log.d(tag, "║ Content-Type: " + contentType.toString());
                }
                long contentLength = partBody.contentLength();
                if (contentLength != -1) {
                    Log.d(tag, "║ Content-Length: " + contentLength);
                }

                ByteArrayOutputStream socket = new ByteArrayOutputStream();
                BufferedSink sink = Okio.buffer(Okio.sink(socket));
                partBody.writeTo(sink);
                sink.flush();
                Log.d(tag, "║ Content: " + socket.toString());
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void printResponseErrorLog(String lineTag, String url, IOException e) {
        String tag = "ResponseErrorLog";
        Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
        Log.d(tag, "║ " + lineTag);
        Log.d(tag, "║ url:" + url);
//        Log.d(tag, tag, e);
        Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }

    public static void printResponseLog(String lineTag, int responseCode, String responseMessage, String responseData, String url) {
        if (!printLog) {
            return;
        }
        String tag = "ResponseLog";
        if (responseData == null) {
            responseData = "response data is null";
        }
        JsonLog.printJson(tag, responseData, lineTag + " responseCode:" + responseCode + ", responseMessage:" + responseMessage + "\nurl:" + url);
    }
}
