package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.ConsultantParam;
import com.kuangji.paopao.dto.result.ConsultantInfo;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.vo.ConsultantUpdateVO;
import com.kuangji.paopao.vo.SettledInConsultantVO;
import com.kuangji.paopao.vo.consulant.ConsultantResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Mybatis Generator 2020/04/09
 */
public interface ConsultantMapper extends BaseMapper<Consultant> {
    ConsultantUpdateVO getConsultantUpdateVO(Integer consultantId);

    SettledInConsultantVO getSettledInConsultantVO(Integer userId);

    ConsultantInfo getConsultantInfo(@Param("consultantId") Integer consultantId);

    List<ConsultantResult> listConsultantResult(ConsultantParam consultantParam);

    int updateSupervisor(String invitationCode);

    int batchUpdateSupervisor(@Param("idList") List<Integer> idList, @Param("invitationCode") String invitationCode);

    List<ConsultantResult> listConsultantVO(ConsultantParam consultantParam);

    Consultant getParentConsultant(Integer consultantUserId);

    int countSupervisor(String invitationCode);

    List<String> listSupervisorCode();

    User getPlatformAccount(String invitationCode);

}