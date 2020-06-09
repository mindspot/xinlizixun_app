package com.framework.http;

import android.util.LongSparseArray;

import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/8/28.
 * http的分析器，用户分析http请求
 * <p>
 * 1、记录下所有的http请求的相关信息(请求URL、请求时间)<br>
 * 2、同一时间，同一请求发送多次的情况，判断到后立即发送给bugly(0.5秒)<br>
 * 3、同一时间有多个请求同时发送，判断到后立即发送给bugly(0.5秒)
 */
public class HttpAnalyzer {

    private static final long TIMEOUT = 1000 * 10;// 删除超过20秒的请求, 防止数据过多
    private static final long TOO_FAST = 50;// 判断在50毫秒内的多个请求

    private final boolean printLog;
    // key:当前时间，value：保存当前时间的请求列表
    private LongSparseArray<List<Entity>> datas = new LongSparseArray<>(100);

    public HttpAnalyzer(boolean printLog) {
        this.printLog = printLog;
    }


    public void analysis(String originUrl, Request request, String lineTag) {
        Entity entity = new Entity(originUrl, request, lineTag);
        delete();// 删除超时的请求，减少内存占用
        analysis(entity);// 分析TOO_FAST时间内的请求状况
        add(datas, entity);// 在analysis之后添加该请求，防止将该请求作为一个TOO_FAST的请求
    }

    /**
     * 防止数据过多，删除超过1分钟的请求
     */
    public void delete() {
        List<Long> deletes = new ArrayList<>(16);
        long timeout = System.currentTimeMillis() - TIMEOUT;
        for (int index = 0; index < datas.size(); index++) {
            long sendTime = datas.keyAt(index);
            if (sendTime < timeout) {
                deletes.add(sendTime);
            }
        }

        for (Long sendTime : deletes) {
            datas.delete(sendTime);
        }
    }


    /**
     * 将在0.5秒内的请求都上报
     */
    private void analysis(Entity entity) {
        List<Long> tooFasts = new ArrayList<>(4);
        long minTime = entity.sendTime - TOO_FAST;
        for (int index = 0; index < datas.size(); index++) {
            long time = datas.keyAt(index);
            if (time >= minTime) {
                tooFasts.add(time);
            }
        }

        if (tooFasts.isEmpty()) {
            return;
        }

        int tooFastCount = 1;
        LongSparseArray<List<Entity>> tooFastLSA = new LongSparseArray<>(tooFasts.size() * 2);
        for (Long tooFastTime : tooFasts) {
            List<Entity> entityList = datas.get(tooFastTime);
            tooFastCount += entityList.size();
            tooFastLSA.put(tooFastTime, entityList);
        }
        add(tooFastLSA, entity);

        postLog(tooFastCount, tooFastLSA);
    }

    private void postLog(int tooFastCount, LongSparseArray<List<Entity>> tooFastLSA) {
        StringBuilder builder = new StringBuilder("在" + TOO_FAST + "毫秒内发送的请求个数：" + tooFastCount + "\n数据：\n");
        for (int index = 0; index < tooFastLSA.size(); index++) {
            long sendTime = tooFastLSA.keyAt(index);
            builder.append(sendTime).append(":\n");

            for (Entity entity : tooFastLSA.valueAt(index)) {
                builder.append(entity.toString()).append("\n");
            }

            builder.append("\n");
        }

        String tipMsg = builder.toString();
        if (printLog) {
            KLog.w(tipMsg);
        }
//        CrashReport.postCatchedException(new Throwable(tipMsg));
    }


    private void add(LongSparseArray<List<Entity>> lsa, Entity entity) {
        List<Entity> entityList = lsa.get(entity.sendTime);
        if (entityList == null) {
            entityList = new ArrayList<>(4);
            lsa.put(entity.sendTime, entityList);
        }
        entityList.add(entity);
    }


    private static final class Entity {
        private final long sendTime;
        private final String requestMethod;
        private final String originUrl;
        private final String tagName;
        private final String lineTag;
        private final Headers headers;
        private final String body;

        //        private final String cacheControlStr;


        Entity(String originUrl, Request request, String lineTag) {
            this.sendTime = System.currentTimeMillis();
            this.requestMethod = request.method();
            this.originUrl = originUrl;
            this.tagName = request.tag() == null ? "tagName" : request.tag().getClass().getName();
            this.lineTag = lineTag;
            this.headers = request.headers();
            this.body = body(request);
//            this.cacheControlStr = request.cacheControl().toString();
        }

        private String body(Request request) {
            RequestBody requestBody = request.body();
            if (requestBody == null) {
                return null;
            }

            if (requestBody instanceof FormBody) {
                return getFormBody((FormBody) requestBody);
            } else if (requestBody instanceof MultipartBody) {
                return "MULTIPART BODY";
            }
            return "UNKNOWN REQUEST BODY";
        }

        private String getFormBody(FormBody body) {
            StringBuilder builder = new StringBuilder();
            builder.append("contentLength:").append(body.contentLength()).append(",").append('\n');
            MediaType contentType = body.contentType();
            builder.append("contentType:").append(contentType.toString()).append(", type:").append(contentType.type())
                    .append(", subtype:").append(contentType.subtype()).append(", charset:").append(contentType.charset()).append('\n');
            for (int i = 0; i < body.size(); i++) {
                builder.append(body.name(i)).append(":").append(body.value(i)).append(",");
            }
            return builder.toString();
        }


        @Override
        public String toString() {
            return "Entity{" +
                    "sendTime=" + sendTime + ", requestMethod='" + requestMethod + '\'' + '\n' +
                    ", originUrl='" + originUrl + '\'' + '\n' +
                    ", tagName='" + tagName + '\'' + ", lineTag='" + lineTag + '\'' + '\n' +
                    ", headersStr='" + headerString() + '\'' + '\n' +
                    ", body='" + body + '\'' +
                    '}';
        }

        private String headerString() {
            StringBuilder builder = new StringBuilder();
            for (String headerName : headers.names()) {
                List<String> headerValues = headers.values(headerName);
                builder.append(headerName).append(":").append(headerValues.toString()).append(",");
            }
            return builder.toString();
        }
    }
}
