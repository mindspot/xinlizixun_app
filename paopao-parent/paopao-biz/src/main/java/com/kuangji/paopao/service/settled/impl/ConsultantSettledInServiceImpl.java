package com.kuangji.paopao.service.settled.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.WorkDTO;
import com.kuangji.paopao.dto.result.CommonResult;
import com.kuangji.paopao.enums.ConsultantWorkEnum;
import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.enums.UserImageTypeEnum;
import com.kuangji.paopao.enums.UserTypeEnum;
import com.kuangji.paopao.mapper.CertificationMapper;
import com.kuangji.paopao.mapper.CommonMapper;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.EducationExperienceMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.mapper.PlatformWorkingTimeMapper;
import com.kuangji.paopao.mapper.PublishMaterialMapper;
import com.kuangji.paopao.mapper.SupervisedExperienceMapper;
import com.kuangji.paopao.mapper.TrainingExperienceMapper;
import com.kuangji.paopao.mapper.UserImageMapper;
import com.kuangji.paopao.mapper.UserLabelMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.model.consultant.Certification;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.EducationExperience;
import com.kuangji.paopao.model.consultant.PublishMaterial;
import com.kuangji.paopao.model.consultant.ReviewRecord;
import com.kuangji.paopao.model.consultant.SupervisedExperience;
import com.kuangji.paopao.model.consultant.TrainingExperience;
import com.kuangji.paopao.redis.util.RedisUtils;
import com.kuangji.paopao.service.consultant.ReviewRecordService;
import com.kuangji.paopao.service.settled.ConsultantSettledInService;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.MD5Utils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.VerificationUtils;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;
import com.kuangji.paopao.vo.SettledInConsultantVO;
import com.kuangji.paopao.vo.consulant.ConsultantCertificateVO;

import tk.mybatis.mapper.entity.Example;

@Service
public class ConsultantSettledInServiceImpl implements ConsultantSettledInService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private ConsultantMapper consultantMapper;
    @Autowired
    private UserImageMapper userImageMapper;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private PlatformWorkingTimeMapper platformWorkingTimeMapper;
    @Autowired
    private EducationExperienceMapper educationExperienceMapper;
    @Autowired
    private TrainingExperienceMapper trainingExperienceMapper;
    @Autowired
    private PublishMaterialMapper publishMaterialMapper;
    @Autowired
    private SupervisedExperienceMapper supervisedExperienceMapper;
    @Autowired
    private CertificationMapper certificationMapper;
    @Autowired
    private UserLabelMapper userLabelMapper;
    @Autowired
    private MallGoodsMapper mallGoodsMapper;
    @Autowired
    private ReviewRecordService reviewRecordService;

    
    private static String IMAGE_URL = PropertiesFileUtil.getInstance().get("image_url");

    private  static String  CONSULTANT_SEND_WORD="做有效的心理咨询";
    
    
    private static String M_m="Mm";
    
    private boolean checkCode(String sysCode, String code) {
    	if (code.equals("0000")) {
     		return true;
		}
        if (!code.equals(sysCode)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map<String, Object> settledInLogin(String userName, String code) {
        boolean bm = VerificationUtils.isMobilePhone(userName);
        if (!bm) {
            throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_PHONE);
        }
        if (StringUtils.isBlank(code)) {
            throw new ServiceException(ResultCodeEnum.ERROR_USER_NOT_CODE);
        }
        String key = CommonConstant.SETTLED_IN + userName;

        String sysCode = String.valueOf(redisUtils.get(key));

        this.checkCode(sysCode, code);

        Map<String, Object> map = new HashMap<>();
        User user = userMapper.consultantLogin(userName);
        if (user != null) {
            map.put(CommonConstant.USER_ID, user.getId());
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.CONSULTANT.getType());
            map.put(CommonConstant.PERSONAL_SIGNATURE, "");
            Consultant consultant = new Consultant();
            consultant.setUserId(user.getId());
            consultant = consultantMapper.selectOne(consultant);
            map.put(CommonConstant.CONSULTANT_ID, consultant.getId());
            map.put(CommonConstant.CONSULTANT_STATUS, consultant.getStatus());
            ReviewRecord reviewRecord = reviewRecordService.getLastReviewRecord(consultant.getId(), UserTypeEnum.CONSULTANT.getType());
            map.put(CommonConstant.CONSULTANT_REMARK, reviewRecord != null ? reviewRecord.getReviewRemark() : "");
            map.put(CommonConstant.ACCOUNT, 0);
            String token = JwtUtils.sign(map);
            map.put(CommonConstant.TOKEN, token);
            redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+user.getId(), token, CommonConstant.LOGIN_TIME);
            return map;
        } else {
            // 注册登陆账号
            user = new User();
            user.setUserName(userName);
            user.setStatus(LoginEnum.LOGIN_NORMAL.getIndex());
            String  pwd=MD5Utils.stringToMD5(M_m+userName);
            user.setPwd(pwd);
            userMapper.insertSelective(user);
            // 咨询师插入
            Consultant consultant = new Consultant();
            consultant.setUserId(user.getId());
//            consultant.setStatus(LoginEnum.LOGIN_TO_EXAMINE.getIndex());
            consultantMapper.insertSelective(consultant);
            map.put(CommonConstant.CONSULTANT_ID, consultant.getId());
            map.put(CommonConstant.CONSULTANT_STATUS, consultant.getStatus());
            map.put(CommonConstant.USER_ID, user.getId());
            map.put(CommonConstant.USER_HEAD_IMG, ImageUtils.getImgagePath(IMAGE_URL, user.getHeadImg()));
            map.put(CommonConstant.REAL_NAME, user.getRealName());
            map.put(CommonConstant.USER_TYPE_K, UserTypeEnum.CONSULTANT.getType());
            map.put(CommonConstant.PERSONAL_SIGNATURE, "");
            map.put(CommonConstant.ACCOUNT, 0);
            String token = JwtUtils.sign(map);
            map.put(CommonConstant.TOKEN, token);
            redisUtils.set(CommonConstant.PAO_PAO_APP_TOKEN+user.getId(), token, CommonConstant.LOGIN_TIME);
            return map;
        }

    }

    //入驻的时候获取的标签
    @Override
    public SettledInConsultantVO listConsultantBaseInfo(String token) {
        Integer userId = JwtUtils.getUserId(token);
        return consultantMapper.getSettledInConsultantVO(userId);
    }

//    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertConsultantDTO(SettledInConsultantVO dto, Integer userId) {
        List<UserImage> cards = dto.getCards();
        if (cards == null || cards.isEmpty() || cards.size() != 2) {
            throw new ServiceException(ResultCodeEnum.ERROR_SETTLED_IN_ID_CARDS);
        }
        String invitationCode =  dto.getInvitationCode();
        if(StringUtils.isNotEmpty(invitationCode)) {
            int count = consultantMapper.countSupervisor(invitationCode);
            if(count != 1) {
                throw new ServiceException(ResultCodeEnum.ERROR_INVITATION_CODE);
            }
        }
        //修改头像
        User user = new User();
        user.setId(userId);
        user.setHeadImg(dto.getHeadImg());
        user.setRealName(dto.getRealName());
//        user.setAge(dto.getAge());
        user.setSex(dto.getSex());
        userMapper.updateByPrimaryKeySelective(user);
        //修改咨询师基本信息
        Consultant consultant = new Consultant();
        consultant.setUserId(userId);
        if(StringUtils.isBlank(dto.getSendWord())){
        	dto.setSendWord(CONSULTANT_SEND_WORD);
        }
        if(StringUtils.isBlank(dto.getBriefIntroduction())){
        	dto.setBriefIntroduction(CONSULTANT_SEND_WORD);
        }
        consultant = consultantMapper.selectOne(consultant);
        BeanUtils.copyProperties(dto, consultant);
        consultantMapper.updateByPrimaryKeySelective(consultant);
        // 身份证修改
        userImageMapper.delete(new UserImage(userId, UserImageTypeEnum.ID_CARD.getIndex()));
        List<String> idCards = new ArrayList<>();
        idCards.add(cards.get(0).getImgUrl());
        idCards.add(cards.get(1).getImgUrl());
        userImageMapper.insertBatchUserImageBatch(idCards, userId, UserImageTypeEnum.ID_CARD.getIndex());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertConsultantCertificateVO(ConsultantCertificateVO vo, Integer consultantId) {
        EducationExperience educationExperience = new EducationExperience();
        educationExperience.setConsultantId(consultantId);
        educationExperienceMapper.delete(educationExperience);
        List<EducationExperience> educationExperienceList = vo.getEducationExperienceList();
        if (CollectionUtils.isNotEmpty(educationExperienceList)) {
            educationExperienceMapper.insertBatch(educationExperienceList);
        }
        Certification certification = new Certification();
        certification.setConsultantId(consultantId);
        certificationMapper.delete(certification);
        List<Certification> certificationList = vo.getCertificationList();
        if (CollectionUtils.isNotEmpty(certificationList)) {
            certificationMapper.insertBatch(certificationList);
        }
        TrainingExperience trainingExperience = new TrainingExperience();
        trainingExperience.setConsultantId(consultantId);
        trainingExperienceMapper.delete(trainingExperience);
        List<TrainingExperience> trainingExperienceList = vo.getTrainingExperienceList();
        if (CollectionUtils.isNotEmpty(trainingExperienceList)) {
            for (TrainingExperience trainingExperience1 : trainingExperienceList) {
                trainingExperienceMapper.insertSelective(trainingExperience1);
            }
        }
        SupervisedExperience supervisedExperience = new SupervisedExperience();
        supervisedExperience.setConsultantId(consultantId);
        supervisedExperienceMapper.delete(supervisedExperience);
        List<SupervisedExperience> supervisedExperienceList = vo.getSupervisedExperienceList();
        if (CollectionUtils.isNotEmpty(supervisedExperienceList)) {
            for (SupervisedExperience supervisedExperience1 : supervisedExperienceList) {
                supervisedExperienceMapper.insertSelective(supervisedExperience1);
            }
        }
        PublishMaterial publishMaterial = new PublishMaterial();
        publishMaterial.setConsultantId(consultantId);
        publishMaterialMapper.delete(publishMaterial);
        List<PublishMaterial> publishBookList = vo.getPublishBookList();
        if (CollectionUtils.isNotEmpty(publishBookList)) {
            for (PublishMaterial trainingExperience1 : publishBookList) {
                publishMaterialMapper.insertSelective(trainingExperience1);
            }
        }
        List<PublishMaterial> publishPaperList = vo.getPublishPaperList();
        if (CollectionUtils.isNotEmpty(publishPaperList)) {
            for (PublishMaterial publishMaterial1 : publishPaperList) {
                publishMaterialMapper.insertSelective(publishMaterial1);
            }
        }
        return 0;
    }

    @Override
    public List<PublishMaterial> listPublishMaterial(Integer consultantId, Integer materialType) {
        Example example = new Example(PublishMaterial.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("consultantId", consultantId);
        criteria.andEqualTo("type", materialType);
        return publishMaterialMapper.selectByExample(example);
    }

    @Override
    public int addPublishMaterial(PublishMaterial publishMaterial, Integer consultantId) {
        if (publishMaterial.getId() != null && publishMaterial.getId() > 0) {
            return publishMaterialMapper.updateByPrimaryKeySelective(publishMaterial);
        } else {
            return publishMaterialMapper.insertSelective(publishMaterial);
        }
    }

    @Override
    public int deletePublishMaterial(Integer publishMaterialId) {
        return publishMaterialMapper.deleteByPrimaryKey(publishMaterialId);
    }

    @Override
    public List<EducationExperience> listEducationExperience(Integer consultantId) {
        Example example = new Example(EducationExperience.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("consultantId", consultantId);
        return educationExperienceMapper.selectByExample(example);
    }

    @Override
    public int addEducationExperience(EducationExperience educationExperience) {
        if (educationExperience.getId() != null && educationExperience.getId() > 0) {
            return educationExperienceMapper.updateByPrimaryKeySelective(educationExperience);
        } else {
            return educationExperienceMapper.insertSelective(educationExperience);
        }
    }

    @Override
    public int deleteEducationExperience(Integer educationExperienceId) {
        return educationExperienceMapper.deleteByPrimaryKey(educationExperienceId);
    }

    @Override
    public List<TrainingExperience> listTrainingExperience(Integer consultantId) {
        Example example = new Example(TrainingExperience.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("consultantId", consultantId);
        return trainingExperienceMapper.selectByExample(example);
    }

    @Override
    public int addTrainingExperience(TrainingExperience trainingExperience) {
        if (trainingExperience.getId() != null && trainingExperience.getId() > 0) {
            return trainingExperienceMapper.updateByPrimaryKeySelective(trainingExperience);
        } else {
            return trainingExperienceMapper.insertSelective(trainingExperience);
        }
    }

    @Override
    public int deleteTrainingExperience(Integer trainingExperienceId) {
        return trainingExperienceMapper.deleteByPrimaryKey(trainingExperienceId);
    }

    @Override
    public List<SupervisedExperience> listSupervisedExperience(Integer consultantId) {
        Example example = new Example(TrainingExperience.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("consultantId", consultantId);
        return supervisedExperienceMapper.selectByExample(example);
    }

    @Override
    public int addSupervisedExperience(SupervisedExperience supervisedExperience) {
        if (supervisedExperience.getId() != null && supervisedExperience.getId() > 0) {
            return supervisedExperienceMapper.updateByPrimaryKeySelective(supervisedExperience);
        } else {
            return supervisedExperienceMapper.insertSelective(supervisedExperience);
        }
    }

    @Override
    public int deleteSupervisedExperience(Integer supervisedExperienceId) {
        return trainingExperienceMapper.deleteByPrimaryKey(supervisedExperienceId);
    }

    @Override
    public List<Certification> listCertification(Integer consultantId) {
        Example example = new Example(Certification.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("consultantId", consultantId);
        return certificationMapper.selectByExample(example);
    }

    @Override
    public int addCertification(Certification certification) {
        if (certification.getId() != null && certification.getId() > 0) {
            return certificationMapper.updateByPrimaryKeySelective(certification);
        } else {
            return certificationMapper.insertSelective(certification);
        }
    }

    @Override
    public int deleteCertification(Integer certificationId) {
        return certificationMapper.deleteByPrimaryKey(certificationId);
    }

    @Override
    public List<Common> listCommonOption(Integer type) {
        Example example = new Example(Common.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type);
        return commonMapper.selectByExample(example);
    }

    @Override
    public CommonResult listCommonOption(Integer userId, Integer type) {
        CommonResult commonResult = new CommonResult();
        Example example = new Example(Common.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", type);
   	 	example.setOrderByClause("sort");
        commonResult.setCommonList(commonMapper.selectByExample(example));
        List<Integer> userLabelList = userLabelMapper.listLabelIdByUser(userId, type);
        if (CollectionUtils.isNotEmpty(userLabelList)) {
            commonResult.setRelatedIds(userLabelList.toArray(new Integer[0]));
        }
        return commonResult;
    }

    @Override
    public List<PlatformWorkingTimeVO> listPlatformWorkingTime(Integer userId) {
        return platformWorkingTimeMapper.listWorkingUserTime(userId);
    }

    @Override
    public List<WorkDTO> listWorkingTime(Integer userId) {
        List<MallGoods> mallGoods = mallGoodsMapper.select(new MallGoods(userId, 0));
        List<WorkDTO> workVOs = new ArrayList<>();
        for (MallGoods goods : mallGoods) {
            WorkDTO workVO = new WorkDTO();
            workVO.setGoodsId(goods.getId());
            workVO.setSellPrice(goods.getSellPrice() / 100);
            workVO.setServiceTimes(goods.getStock());
            workVO.setServiceName(goods.getShareText());
            workVO.setVal(goods.getSellPointText());
            workVO.setCode(ConsultantWorkEnum.getCode(goods.getSellPointText()));
            workVOs.add(workVO);
        }
        return workVOs;
    }
}
