package com.kuangji.paopao.vo;

import lombok.Data;

//咨询师资质显示
@Data
public class QualificationsVO {
    private int id;
    private int qualifications;
    private String qualificationsName;
    private String qualificationsNumber;
    private int qualificationsYears;
    private int practitionersYears;
    private int caseNumber;
    private String imgUrl;
    private String imgUrlSize;

}
