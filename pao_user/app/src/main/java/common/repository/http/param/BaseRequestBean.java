package common.repository.http.param;

import com.framework.http.bean.RequestBean;

import java.util.HashMap;
import java.util.Map;


public class BaseRequestBean extends RequestBean {

    private Map<String, String> header = new HashMap<>();

    @Override
    public Object getTag() {
        return getClassName();
    }

    @Override
    public Map<String, String> getHeaders() {
        header.putAll(RequestHeaderHelper.getCookies());
        header.putAll(RequestHeaderHelper.getHeaders());
        return header;
    }

    public void addHeader(String key, String value) {
        this.header.put(key, value);
    }

    public void addGzipHeader() {
        addHeader("Content-Encoding", "gzip");
    }

    @Override
    public boolean isSetCache() {
        return false;
    }

    @Override
    public HashMap<String, String> getParams() {
        return null;
    }

}
