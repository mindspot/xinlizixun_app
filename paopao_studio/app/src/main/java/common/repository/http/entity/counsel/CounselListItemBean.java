package common.repository.http.entity.counsel;

import java.util.List;

/**
 * Created by hpzhan on 2020/2/22.
 */

public class CounselListItemBean {
    private int id;
    private String realName;
    private String headImg;
    private String provName;
    private String cityName;
    private String areaName;
    private String addrDetail;
    private String content;
    private int consultationFee;
    private List<LabelBean> consultantLabelVOs;
    private String easemobId;
    private String displayContent;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAddrDetail() {
        return addrDetail;
    }

    public void setAddrDetail(String addrDetail) {
        this.addrDetail = addrDetail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(int consultationFee) {
        this.consultationFee = consultationFee;
    }

    public List<LabelBean> getConsultantLabelVOs() {
        return consultantLabelVOs;
    }

    public void setConsultantLabelVOs(List<LabelBean> consultantLabelVOs) {
        this.consultantLabelVOs = consultantLabelVOs;
    }

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
    }

    public String getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(String displayContent) {
        this.displayContent = displayContent;
    }

    public static class LabelBean {
        private String labelVal;
        private int labelId;

        public String getLabelVal() {
            return labelVal;
        }

        public void setLabelVal(String labelVal) {
            this.labelVal = labelVal;
        }

        public int getLabelId() {
            return labelId;
        }

        public void setLabelId(int labelId) {
            this.labelId = labelId;
        }
    }
}
