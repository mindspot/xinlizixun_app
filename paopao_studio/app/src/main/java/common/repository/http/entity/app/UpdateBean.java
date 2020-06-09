package common.repository.http.entity.app;

import java.io.Serializable;

public class UpdateBean implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1509609039656752500L;

    private int has_upgrade;//是否有升级
    private int is_force_upgrade;//是否强制升级
    private String new_version;//新版本号
    private String new_features;//新版本更新说明
    private String ard_url;//新版本下载地址
    private String ard_size;//新版本更新包大小

    public int getHas_upgrade() {
        return has_upgrade;
    }

    public void setHas_upgrade(int has_upgrade) {
        this.has_upgrade = has_upgrade;
    }

    public int getIs_force_upgrade() {
        return is_force_upgrade;
    }

    public void setIs_force_upgrade(int is_force_upgrade) {
        this.is_force_upgrade = is_force_upgrade;
    }

    public String getNew_version() {
        return new_version;
    }

    public void setNew_version(String new_version) {
        this.new_version = new_version;
    }

    public String getNew_features() {
        return new_features;
    }

    public void setNew_features(String new_features) {
        this.new_features = new_features;
    }

    public String getArd_url() {
        return ard_url;
    }

    public void setArd_url(String ard_url) {
        this.ard_url = ard_url;
    }

    public String getArd_size() {
        return ard_size;
    }

    public void setArd_size(String ard_size) {
        this.ard_size = ard_size;
    }
}
