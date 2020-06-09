package common.repository.http.entity.order.councilor;

import java.io.Serializable;
import java.util.List;

/**
 * @version V 1.0 xxxxxxxx
 * @Title: pao_studio
 * @Package common.repository.http.entity.order.councilor
 * @Description: $todo$
 * @author: L-BackPacker
 * @date: 2020.03.26 上午 8:50
 * @verdescript 版本号 修改时间  修改人 修改的概要说明
 * @Copyright: 2020
 */
public class CouncilorItemBean implements Serializable {
    private String orderNo;
    private int orderCode;
    private String realName;
    private String headImg;
    private List<String> labelVOs;
    private List<ConsultantSupervisorOrderDetailsBean> listConsultantSupervisorOrderDetailsVO;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public List<String> getLabelVOs() {
        return labelVOs;
    }

    public void setLabelVOs(List<String> labelVOs) {
        this.labelVOs = labelVOs;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public void setListConsultantSupervisorOrderDetailsVO(List<ConsultantSupervisorOrderDetailsBean> listConsultantSupervisorOrderDetailsVO) {
        this.listConsultantSupervisorOrderDetailsVO = listConsultantSupervisorOrderDetailsVO;
    }

    public List<ConsultantSupervisorOrderDetailsBean> getListConsultantSupervisorOrderDetailsVO() {
        return listConsultantSupervisorOrderDetailsVO;
    }

    public static class ConsultantSupervisorOrderDetailsBean {
        private String mallTradOrderNo;
        private int consultantId;

        public String getMallTradOrderNo() {
            return mallTradOrderNo;
        }

        public void setMallTradOrderNo(String mallTradOrderNo) {
            this.mallTradOrderNo = mallTradOrderNo;
        }

        public int getConsultantId() {
            return consultantId;
        }

        public void setConsultantId(int consultantId) {
            this.consultantId = consultantId;
        }
    }
}
