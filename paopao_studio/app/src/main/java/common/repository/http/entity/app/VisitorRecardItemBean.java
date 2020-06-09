package common.repository.http.entity.app;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_studio
 * @Package common.repository.http.entity.balance
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.27 下午 5:27
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class VisitorRecardItemBean {
    private String headImg;
    private String realName;
    private String easemobId;
    private String visitorTime;
    private int isOrder;//1 在平台下个单 0在平台没有下过单

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getEasemobId() {
        return easemobId;
    }

    public void setEasemobId(String easemobId) {
        this.easemobId = easemobId;
    }

    public String getVisitorTime() {
        return visitorTime;
    }

    public void setVisitorTime(String visitorTime) {
        this.visitorTime = visitorTime;
    }

    public int getIsOrder() {
        return isOrder;
    }

    public void setIsOrder(int isOrder) {
        this.isOrder = isOrder;
    }
}
