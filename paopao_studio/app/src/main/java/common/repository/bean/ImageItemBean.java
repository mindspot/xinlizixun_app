package common.repository.bean;

import java.io.File;

/**
 * Created by hpzhan on 2019/4/19.
 */

public class ImageItemBean {
    private int type;
    private File file;
    private String path;
    private String imgSize;

    public ImageItemBean(int type) {
        this.type = type;
    }

    public ImageItemBean(File file) {
        this.file = file;
    }

    public ImageItemBean(String path, String imgSize) {
        this.path = path;
        this.imgSize = imgSize;
        this.type = 2;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImgSize() {
        return imgSize;
    }

    public void setImgSize(String imgSize) {
        this.imgSize = imgSize;
    }
}
