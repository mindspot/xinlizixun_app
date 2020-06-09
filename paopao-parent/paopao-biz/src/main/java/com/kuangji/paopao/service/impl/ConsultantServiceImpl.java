package com.kuangji.paopao.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.dto.UpdateImgeDTO;
import com.kuangji.paopao.dto.param.ConsultantParam;
import com.kuangji.paopao.dto.result.ConsultantInfo;
import com.kuangji.paopao.enums.ConsultantTypeEnum;
import com.kuangji.paopao.enums.LoginEnum;
import com.kuangji.paopao.mapper.CertificationMapper;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.ConsultantRateMapper;
import com.kuangji.paopao.mapper.EducationExperienceMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.mapper.UserImageMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.model.consultant.Certification;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.ConsultantRate;
import com.kuangji.paopao.model.consultant.EducationExperience;
import com.kuangji.paopao.redis.Easemob;
import com.kuangji.paopao.service.ConsultantService;
import com.kuangji.paopao.service.base.impl.BaseServiceImpl;
import com.kuangji.paopao.service.settled.ConsultantSettledInService;
import com.kuangji.paopao.util.ImageUtils;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.util.TransferUtils;
import com.kuangji.paopao.vo.ConsultantUpdateVO;
import com.kuangji.paopao.vo.consulant.ConsultantResult;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@Service
public class ConsultantServiceImpl extends BaseServiceImpl<Consultant, Integer> implements ConsultantService {
    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MallGoodsMapper mallGoodsMapper;
    @Autowired
    private Easemob easemob;
    @Autowired
    private ConsultantSettledInService consultantSettledInService;
    @Autowired
    private ConsultantRateMapper consultantRateMapper;
    @Autowired
    private CertificationMapper certificationMapper;
    @Autowired
    private UserImageMapper userImageMapper;
    @Autowired
    private EducationExperienceMapper educationExperienceMapper;
    
    private static String imageUrl = PropertiesFileUtil.getInstance().get("image_url");


    //环信 用户 标志
    public static final String member = PropertiesFileUtil.getInstance().get("easemob_member");


    //环信 用户 标志
    public static final String consultant = PropertiesFileUtil.getInstance().get("easemob_consultant");

    //环信 咨询师标志easemob_consultant
    public static final String pwd = PropertiesFileUtil.getInstance().get("easemob_pwd");

    private static final String[] prohibitRealNames = {"系统消息", "admin"};

    @Override
    public ConsultantUpdateVO getConsultantUpdateVO(Integer consultantId) {
        ConsultantUpdateVO vo = consultantMapper.getConsultantUpdateVO(consultantId);
        if (vo != null) {
            vo.setHeadImg(ImageUtils.getImgagePath(imageUrl, vo.getHeadImg()));
        }
        return vo;
    }


    @Override
    public BaseMapper<Consultant> getMapper() {
        // TODO Auto-generated method stub
        return consultantMapper;
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public int updateConsultantVO(Integer consultantId, ConsultantUpdateVO consultantUpdateVO) {
        User user = userMapper.selectByPrimaryKey(consultantId);

        String headImg = consultantUpdateVO.getHeadImg();
        if (StringUtils.isNotBlank(headImg)) {
            user.setHeadImg(headImg);
        }

        String realName = consultantUpdateVO.getRealName();
        if (StringUtils.isNotBlank(realName)) {
            for (String prohibitRealName : prohibitRealNames) {
                if (prohibitRealName.equals(realName)) {
                    throw new ServiceException(ResultCodeEnum.ERROR_LOGIN_NOT_REALNAME);
                }
            }
            user.setRealName(realName);
        }
        Integer sex = consultantUpdateVO.getSex();
        if (sex != null) {
            user.setSex(sex);
        }
        user.setAccount(null);
        user.setCashWithdrawalAmount(null);
        userMapper.updateByPrimaryKeySelective(user);

        Example example = new Example(Consultant.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", consultantId);
        Consultant consultant = consultantMapper.selectOneByExample(example);
        consultant.setSendWord(consultantUpdateVO.getSendWord());
        consultant.setBriefIntroduction(consultantUpdateVO.getContent());
        consultantMapper.updateByPrimaryKeySelective(consultant);
        return 1;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int consultantUpdateStatus(Integer id) {

        easemob.createUser(consultant + id, pwd);
        Consultant c = new Consultant();
        c.setId(id);
        c.setStatus(LoginEnum.LOGIN_NORMAL.getIndex());
        consultantMapper.updateByPrimaryKeySelective(c);
        return mallGoodsMapper.upDownByShopId(id);
    }


    @Override
    public int updateSupervisor(Integer id, String invitationCode) {
        if (StringUtils.isNotEmpty(invitationCode)) {
            int count = consultantMapper.countSupervisor(invitationCode);
            if(count>0) {
                throw new ServiceException(ResultCodeEnum.INVITATION_CODE_EXISTED);
            }
            Consultant consultant = consultantMapper.selectByPrimaryKey(id);
            if (consultant.getUserType() == ConsultantTypeEnum.CONSULTANT.getType()) {
                //设置为督导
                consultant.setUserType(ConsultantTypeEnum.SUPERVISOR.getType());
                consultant.setInvitationCode(invitationCode);
                // 默认分成比列设置
                ConsultantRate firstConsultantRate =new ConsultantRate();
                firstConsultantRate.setConsultantId(id);
                firstConsultantRate.setPlatformRate(16);
                firstConsultantRate.setChannelRate(5);
                firstConsultantRate.setPartnerRate(4);
                firstConsultantRate.setConsultantRate(75);
                firstConsultantRate.setType(1);
                ConsultantRate nextConsultantRate =new ConsultantRate();
                nextConsultantRate.setConsultantId(id);
                nextConsultantRate.setPlatformRate(12);
                nextConsultantRate.setChannelRate(0);
                nextConsultantRate.setPartnerRate(3);
                nextConsultantRate.setConsultantRate(85);
                nextConsultantRate.setType(2);
                consultantRateMapper.insertSelective(firstConsultantRate);
                consultantRateMapper.insertSelective(nextConsultantRate);
                return consultantMapper.updateByPrimaryKeySelective(consultant);
            }
        }
        return 0;
    }


    @Override
    public int updateCancelSupervisor(Integer id) {
        Consultant consultant = new Consultant();
        consultant.setId(id);
        consultant.setUserType(ConsultantTypeEnum.CONSULTANT.getType());
        ConsultantRate consultantRate=new ConsultantRate();
        consultantRate.setConsultantId(id);
        consultantRateMapper.delete(consultantRate);
        return consultantMapper.updateByPrimaryKeySelective(consultant);
    }

    @Override
    public int update(Integer id, String invitationCode, Integer sort) {
        Consultant consultant = new Consultant();
        consultant.setId(id);
        consultant.setInvitationCode(invitationCode);
        consultant.setSort(sort);
        Example example = new Example(Consultant.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userType", 1);
        criteria.andEqualTo("invitationCode", invitationCode);
        int count = consultantMapper.selectCountByExample(example);
        if (count > 1) {
            throw new ServiceException(ResultCodeEnum.INVITATION_CODE_EXIST);
        }
        return consultantMapper.updateByPrimaryKeySelective(consultant);
    }

    @Override
    public ConsultantInfo getConsultantInfo(Integer consultantId) {
        ConsultantInfo consultantInfo = consultantMapper.getConsultantInfo(consultantId);
        ConsultantParam consultantParam=new ConsultantParam();
        consultantParam.setInvitationCode(consultantInfo.getInvitationCode());
        consultantInfo.setConsultantList(consultantMapper.listConsultantResult(consultantParam));
        consultantInfo.setWorkTimeList(consultantSettledInService.listWorkingTime(consultantInfo.getUserId()));
        return consultantInfo;
    }

    @Override
    public List<ConsultantResult> listConsultantVO(ConsultantParam consultantParam) {
        PageHelper.startPage(consultantParam.getPageNo(), consultantParam.getPageSize());
        return consultantMapper.listConsultantVO(consultantParam);
    }

    @Override
    public List<ConsultantResult> listConsultantResult(ConsultantParam consultantParam) {
        PageHelper.startPage(consultantParam.getPageNo(), consultantParam.getPageSize());
        return consultantMapper.listConsultantResult(consultantParam);
    }

    @Override
    @Transactional
    public int changeSupervisorList(String consultantIds, String invitationCode) {
        consultantMapper.updateSupervisor(invitationCode);
        if (StringUtils.isNotEmpty(consultantIds)) {
            List<Integer> idList = TransferUtils.strToIntegers(consultantIds);
            consultantMapper.batchUpdateSupervisor(idList, invitationCode);
        }
        return 1;
    }

    @Override
    public int updateConsultantRate(ConsultantResult consultantResult) {
        ConsultantRate firstConsultantRate = consultantResult.getFirstConsultantRate();
        ConsultantRate nextConsultantRate = consultantResult.getNextConsultantRate();
        if (firstConsultantRate.getId() > 0) {
            consultantRateMapper.updateByPrimaryKeySelective(firstConsultantRate);
        } else {
            consultantRateMapper.insertSelective(firstConsultantRate);

        }
        if (nextConsultantRate.getId() > 0) {
            consultantRateMapper.updateByPrimaryKeySelective(nextConsultantRate);

        } else {
            consultantRateMapper.insertSelective(nextConsultantRate);
        }
        return 1;
    }

    @Override
    public ConsultantRate getConsultantRate(Integer consultantUserId, Integer type) {
        Consultant parentConsultant = consultantMapper.getParentConsultant(consultantUserId);
        ConsultantRate consultantRate = null;
        if (type == 1) {
            consultantRate = consultantRateMapper.getFirstConsultantRate(parentConsultant.getId());
        }
        if (type == 2) {
            consultantRate = consultantRateMapper.getSecondConsultantRate(parentConsultant.getId());
        }
        return consultantRate;
    }

    @Override
    public List<String> listSupervisorCode() {
        return consultantMapper.listSupervisorCode();
    }


	@Override
	public int updateImage(UpdateImgeDTO dto) {
		switch (dto.getType()) {
		//修改头像
		case 1:
			User user =new User();
			user.setId(dto.getId());
			user.setHeadImg(dto.getSrc());
			userMapper.updateByPrimaryKeySelective(user);
			break;
		//修改资质图片	
		case 2:
			Certification certification=new Certification();
			certification.setId(dto.getId());
			certification.setCertificateUrl(dto.getSrc());
			certificationMapper.updateByPrimaryKeySelective(certification);
			break;
		//修改身份证
		case 3:
			UserImage userImage=new UserImage();
			userImage.setId(dto.getId());
			userImage.setImgUrl(dto.getSrc());
			userImageMapper.updateByPrimaryKeySelective(userImage);
			break;
		//修改学历
		case 4:
			EducationExperience educationExperience=new EducationExperience();
			educationExperience.setId(dto.getId());
			educationExperience.setCertificateUrl(dto.getSrc());
			educationExperienceMapper.updateByPrimaryKeySelective(educationExperience);
			break;
		default:
			break;
		}
		return 1;
	}


	@Override
	public int delImage(UpdateImgeDTO dto) {
		switch (dto.getType()) {
		//删除资质图片
		case 2:
			certificationMapper.deleteByPrimaryKey(dto.getId());
			break;
		//删除身份证	
		case 3:
			userImageMapper.deleteByPrimaryKey(dto.getId());
			break;
		case 4:
			educationExperienceMapper.deleteByPrimaryKey(dto.getId());
			break;	
		default:
			break;
		}
		return 1;
	}
}
