package com.kuangji.paopao.vo;

import com.kuangji.paopao.model.UserImage;
import lombok.Data;

import java.util.List;

@Data
public class SettledInConsultantVO {
    private Integer userId;

    private Integer briefIntroductionId;

    private String headImg = "";

    private String realName = "";

    private Integer sex = 0;

    private String birthDate;

    private String provName = "";

    private Integer provCode = 0;

    private String cityName = "";

    private Integer cityCode = 0;

    private String areaName = "";

    private Integer areaCode = 0;

    private String addrDetail = "";

    private String phone;

    private String weixing = "";

    private Float workingSeniority;

    private Integer accumulativeCase;

    private String mailbox = "";

    private String sendWord = "";

    private String briefIntroduction = "";

    private String invitationCode = "";

    private List<UserImage> cards;

}
