package common.repository.http.param;

import java.util.HashMap;
import java.util.Map;

public class FileBean extends BaseRequestBean {
    private String fileSrc;//文件路径
    private String upLoadKey;//上传文件的字段
    private HashMap<String, String> extarParms = new HashMap<>();

    public void setFileSrc(String fileSrc) {
        this.fileSrc = fileSrc;
    }

    public String getFileSrc() {
        return fileSrc;
    }

    public String getUpLoadKey() {
        return upLoadKey;
    }

    public void setUpLoadKey(String upLoadKey) {
        this.upLoadKey = upLoadKey;
    }

    public void addExtraParms(String key, String value) {
        extarParms.put(key, value);
    }

    public void addExtraMap(Map<String, String> params) {
        extarParms.putAll(params);
    }

    public HashMap<String, String> getExtraParms() {
        return extarParms;
    }

    @Override
    public HashMap<String, String> getParams() {
        return super.getParams();
    }

    public HashMap<String, String> getExtarParms() {
        return extarParms;
    }

    public void setExtarParms(HashMap<String, String> extarParms) {
        this.extarParms = extarParms;
    }
}