package common.repository.http.entity.order.councilor;

import java.util.List;

import common.repository.http.param.BaseResponseBean;

/**
 * Created by hpzhan on 2020/2/24.
 */

public class CouncilorOrderDetailResponseBean extends BaseResponseBean {
    private String orderNo;
    private String orderTime;
    private String workType;
    private String goodsName;
    private int goodsAmount;
    private int orderAmount;
    private int orderCode;
    private int proportion;
    private List<String> labels;
    private int supervisorOrderNum;
    private String realName;
    private String easemobId;
    private String headImg;
    private List<SupervisorOrderDetailBean> listSupervisorOrderDetails;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(int goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public int getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(int orderCode) {
        this.orderCode = orderCode;
    }

    public int getProportion() {
        return proportion;
    }

    public void setProportion(int proportion) {
        this.proportion = proportion;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public int getSupervisorOrderNum() {
        return supervisorOrderNum;
    }

    public void setSupervisorOrderNum(int supervisorOrderNum) {
        this.supervisorOrderNum = supervisorOrderNum;
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

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public List<SupervisorOrderDetailBean> getListSupervisorOrderDetails() {
        return listSupervisorOrderDetails;
    }

    public void setListSupervisorOrderDetails(List<SupervisorOrderDetailBean> listSupervisorOrderDetails) {
        this.listSupervisorOrderDetails = listSupervisorOrderDetails;
    }

    public static class SupervisorOrderDetailBean {
        private boolean isDiagnosis;
        private boolean isOpen;
        private String orderInfo;
        private MemberCaseBean memberCase;

        public String getOrderInfo() {
            return orderInfo;
        }

        public void setOrderInfo(String orderInfo) {
            this.orderInfo = orderInfo;
        }

        public MemberCaseBean getMemberCase() {
            return memberCase;
        }

        public void setMemberCase(MemberCaseBean memberCase) {
            this.memberCase = memberCase;
        }

        public boolean isDiagnosis() {
            return isDiagnosis;
        }

        public void setDiagnosis(boolean diagnosis) {
            isDiagnosis = diagnosis;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public void setOpen(boolean open) {
            isOpen = open;
        }
    }

    public static class OrderInfoBean {
        private String orderTime;
        private String realName;

        public String getOrderTime() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime = orderTime;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }
    }

    public static class MemberCaseBean {
        private int id;
        private int userId;
        private String consultationTime;
        private String operName;
        private int sex;
        private String age;
        private boolean isConsultant;
        private List<CommonItem> commonVOs;
        private String detailedDescription;
        private String consultatName;
        private ConsultantOrderDiagnosisBean consultantOrderDiagnosisVO;
        private List<ConsultantOrderDiagnosisBean> consultantOrderDiagnosisVOs;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getConsultationTime() {
            return consultationTime;
        }

        public void setConsultationTime(String consultationTime) {
            this.consultationTime = consultationTime;
        }

        public String getOperName() {
            return operName;
        }

        public void setOperName(String operName) {
            this.operName = operName;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public boolean isConsultant() {
            return isConsultant;
        }

        public void setConsultant(boolean consultant) {
            isConsultant = consultant;
        }

        public List<CommonItem> getCommonVOs() {
            return commonVOs;
        }

        public void setCommonVOs(List<CommonItem> commonVOs) {
            this.commonVOs = commonVOs;
        }

        public String getDetailedDescription() {
            return detailedDescription;
        }

        public void setDetailedDescription(String detailedDescription) {
            this.detailedDescription = detailedDescription;
        }

        public String getConsultatName() {
            return consultatName;
        }

        public void setConsultatName(String consultatName) {
            this.consultatName = consultatName;
        }

        public ConsultantOrderDiagnosisBean getConsultantOrderDiagnosisVO() {
            return consultantOrderDiagnosisVO;
        }

        public void setConsultantOrderDiagnosisVO(ConsultantOrderDiagnosisBean consultantOrderDiagnosisVO) {
            this.consultantOrderDiagnosisVO = consultantOrderDiagnosisVO;
        }

        public List<ConsultantOrderDiagnosisBean> getConsultantOrderDiagnosisVOs() {
            return consultantOrderDiagnosisVOs;
        }

        public void setConsultantOrderDiagnosisVOs(List<ConsultantOrderDiagnosisBean> consultantOrderDiagnosisVOs) {
            this.consultantOrderDiagnosisVOs = consultantOrderDiagnosisVOs;
        }
    }


    public static class ConsultantOrderDiagnosisBean {
        private String content;
        private String realName;
        private String imgeSize;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public String getImgeSize() {
            return imgeSize;
        }

        public void setImgeSize(String imgeSize) {
            this.imgeSize = imgeSize;
        }
    }

    public static class CommonItem {
        private String val;

        public String getVal() {
            return val;
        }

        public void setVal(String val) {
            this.val = val;
        }
    }
}
