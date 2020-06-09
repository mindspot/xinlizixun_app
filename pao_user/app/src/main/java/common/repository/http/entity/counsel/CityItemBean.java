package common.repository.http.entity.counsel;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hpzhan on 2020/2/21.
 */

public class CityItemBean implements Serializable {
    private String areaCode;
    private String areaName;
    private int id;
    private int parentId;
    private List<CityItemBean> dictAreas;

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public List<CityItemBean> getDictAreas() {
        return dictAreas;
    }

    public void setDictAreas(List<CityItemBean> dictAreas) {
        this.dictAreas = dictAreas;
    }
}
