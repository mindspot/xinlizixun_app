package com.kuangji.paopao.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.SetInDTO;
import com.kuangji.paopao.dto.WorkDTO;
import com.kuangji.paopao.dto.result.CommonResult;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.enums.MaterialTypeEnum;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.consultant.Certification;
import com.kuangji.paopao.model.consultant.EducationExperience;
import com.kuangji.paopao.model.consultant.PublishMaterial;
import com.kuangji.paopao.model.consultant.SupervisedExperience;
import com.kuangji.paopao.model.consultant.TrainingExperience;
import com.kuangji.paopao.service.SettledInService;
import com.kuangji.paopao.service.settled.ConsultantSettledInService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;
import com.kuangji.paopao.vo.SettledInConsultantVO;
import com.kuangji.paopao.vo.consulant.ConsultantCertificateVO;

@RestController

public class ConsultantSettledInController {
    @Autowired
    private ConsultantSettledInService consultantSettledInService;
    @Autowired
    private SettledInService  settledInService;
    //咨询端入驻登入
    @PostMapping(value = "/settled-in/login")
    public Object settledInLogin(@RequestParam(defaultValue = "") String phone,
                                 @RequestParam(defaultValue = "") String code) {
        if (StringUtils.isBlank(phone)) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_EMP_USER_NAME);
        }
        Map<String, Object> map = consultantSettledInService.settledInLogin(phone, code);

        return ServiceResultUtils.success(map);
    }

    //获取入驻平台的标签
    @GetMapping("/v1/settled-in/get-info")
    public Object listConsultantBaseInfo(HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        SettledInConsultantVO inConsultantVO = consultantSettledInService.listConsultantBaseInfo(token);
        return ServiceResultUtils.success(inConsultantVO);
    }

    //咨询师基本信息入驻
    @PostMapping(value = "/v1/settled-in/insert-consultant")
    public Object insertConsultantInfo(@RequestBody SettledInConsultantVO dto, HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        consultantSettledInService.insertConsultantDTO(dto, userId);
        return ServiceResultUtils.success(null);
    }

    @PostMapping(value = "/v1/settled-in/insert-consultant-extra-info")
    public Object addConsultantExtraInfo(@RequestBody ConsultantCertificateVO consultantExtraInfo, HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer consultantId = JwtUtils.getConsultantId(token);
        int result = consultantSettledInService.insertConsultantCertificateVO(consultantExtraInfo, consultantId);
        return ServiceResultUtils.success(result > 0);
    }
    @GetMapping(value = "/v1/settled-in/list-publishBook")
    public Object listPublishBook(Integer consultantId) {
        List<PublishMaterial> list = consultantSettledInService.listPublishMaterial(consultantId, MaterialTypeEnum.BOOK.getType());
        return ServiceResultUtils.success(list);
    }

    @GetMapping(value = "/v1/settled-in/list-publishPaper")
    public Object listPublishPaper(Integer consultantId) {
        List<PublishMaterial> list = consultantSettledInService.listPublishMaterial(consultantId, MaterialTypeEnum.PAPER.getType());
        return ServiceResultUtils.success(list);
    }
    @PostMapping(value = "/v1/settled-in/insert-publishMaterial")
    public Object addPublishMaterial(@RequestBody PublishMaterial publishMaterial, HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer consultantId = JwtUtils.getConsultantId(token);
        int result = consultantSettledInService.addPublishMaterial(publishMaterial, consultantId);
        return ServiceResultUtils.success(result > 0);
    }

    @PostMapping(value = "/v1/settled-in/delete-publishMaterial")
    public Object deletePublishMaterial(Integer publishMaterialId) {
        int result = consultantSettledInService.deletePublishMaterial(publishMaterialId);
        return ServiceResultUtils.success(result > 0);
    }

    @GetMapping(value = "/v1/settled-in/list-education")
    public Object listEducationExperience(Integer consultantId) {
        List<EducationExperience> list = consultantSettledInService.listEducationExperience(consultantId);
        return ServiceResultUtils.success(list);
    }

    @PostMapping(value = "/v1/settled-in/insert-education")
    public Object addEducationExperience(@RequestBody EducationExperience educationExperience) {
        int result = consultantSettledInService.addEducationExperience(educationExperience);
        return ServiceResultUtils.success(result > 0);
    }

    @PostMapping(value = "/v1/settled-in/delete-education")
    public Object deleteEducationExperience(Integer educationExperienceId) {
        int result = consultantSettledInService.deleteEducationExperience(educationExperienceId);
        return ServiceResultUtils.success(result > 0);
    }

    @GetMapping(value = "/v1/settled-in/list-training")
    public Object listTrainingExperience(Integer consultantId) {
        List<TrainingExperience> list = consultantSettledInService.listTrainingExperience(consultantId);
        return ServiceResultUtils.success(list);
    }

    @PostMapping(value = "/v1/settled-in/insert-training")
    public Object addTrainingExperience(@RequestBody TrainingExperience trainingExperience) {
        int result = consultantSettledInService.addTrainingExperience(trainingExperience);
        return ServiceResultUtils.success(result > 0);
    }

    @PostMapping(value = "/v1/settled-in/delete-training")
    public Object deleteTrainingExperience(Integer trainingExperienceId) {
        int result = consultantSettledInService.deleteTrainingExperience(trainingExperienceId);
        return ServiceResultUtils.success(result > 0);
    }

    @GetMapping(value = "/v1/settled-in/list-supervised")
    public Object listSupervisedExperience(Integer consultantId) {
        List<SupervisedExperience> list = consultantSettledInService.listSupervisedExperience(consultantId);
        return ServiceResultUtils.success(list);
    }

    @PostMapping(value = "/v1/settled-in/insert-supervised")
    public Object addSupervisedExperience(@RequestBody SupervisedExperience supervisedExperience) {
        int result = consultantSettledInService.addSupervisedExperience(supervisedExperience);
        return ServiceResultUtils.success(result > 0);
    }

    @PostMapping(value = "/v1/settled-in/delete-supervised")
    public Object deleteSupervisedExperience(Integer supervisedExperienceId) {
        int result = consultantSettledInService.deleteSupervisedExperience(supervisedExperienceId);
        return ServiceResultUtils.success(result > 0);
    }

    @GetMapping(value = "/v1/settled-in/list-certification")
    public Object listCertification(Integer consultantId) {
        List<Certification> list = consultantSettledInService.listCertification(consultantId);
        return ServiceResultUtils.success(list);
    }

    @PostMapping(value = "/v1/settled-in/insert-certification")
    public Object addCertification(@RequestBody Certification certification) {
        int result = consultantSettledInService.addCertification(certification);
        return ServiceResultUtils.success(result > 0);
    }

    @PostMapping(value = "/v1/settled-in/delete-certification")
    public Object deleteCertification(Integer certificationId) {
        int result = consultantSettledInService.deleteCertification(certificationId);
        return ServiceResultUtils.success(result > 0);
    }

    @GetMapping(value = "/v1/option/list-certification-type")
    public Object listCertificationType() {
        List<Common> list = consultantSettledInService.listCommonOption(CommonEnum.CERTIFICATE.getVal());
        return ServiceResultUtils.success(list);
    }

    @GetMapping(value = "/v1/option/list-target-people")
    public Object listTargetPeople(HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer  userId = JwtUtils.getUserId(token);
        CommonResult result = consultantSettledInService.listCommonOption(userId,CommonEnum.CONTENTION_TYPE.getVal());
        return ServiceResultUtils.success(result);
    }
    @GetMapping(value = "/v1/option/list-expertise-area")
    public Object listExpertiseArea(HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        CommonResult result = consultantSettledInService.listCommonOption(userId,CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
        return ServiceResultUtils.success(result);
    }
    @GetMapping(value = "/v1/option/list-platform-working-time")
    public Object listPlatformWorkingTime(HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        List<PlatformWorkingTimeVO> list = consultantSettledInService.listPlatformWorkingTime(userId);
        return ServiceResultUtils.success(list);
    }
    @GetMapping(value = "/v1/settled-in/list-working-time")
    public Object listWorkingTime(HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId = JwtUtils.getUserId(token);
        List<WorkDTO> works = consultantSettledInService.listWorkingTime(userId);
        return ServiceResultUtils.success(works);
    }
    @PostMapping(value = "/v1/settled-in/insert-work")
    public Object insertWork(@RequestBody SetInDTO setInDTO,HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
    	settledInService.insertSetInDTO(setInDTO, userId);
        return ServiceResultUtils.success(null);
    }
    @PostMapping(value = "/v1/settled-in/update-work")
    public Object updateWork(@RequestBody SetInDTO setInDTO,HttpServletRequest request) {
        String token = request.getHeader("authorization").replace("Bearer ", "");
        Integer userId=JwtUtils.getUserId(token);
        settledInService.updateSetInDTO(setInDTO, userId);
        return ServiceResultUtils.success(null);
    }
}
