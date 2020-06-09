package com.kuangji.paopao.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.constant.CommonConstant;
import com.kuangji.paopao.dto.SetInDTO;
import com.kuangji.paopao.dto.WorkDTO;
import com.kuangji.paopao.enums.BookingTimeEnum;
import com.kuangji.paopao.enums.CommonEnum;
import com.kuangji.paopao.enums.ConsultantWorkEnum;
import com.kuangji.paopao.enums.MallGoodsClassEnum;
import com.kuangji.paopao.enums.OperateTypeEnum;
import com.kuangji.paopao.enums.ReviewStatusEnum;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.ConsultantScheduleMapper;
import com.kuangji.paopao.mapper.MallGoodsMapper;
import com.kuangji.paopao.mapper.PlatformWorkingTimeMapper;
import com.kuangji.paopao.mapper.UserImageMapper;
import com.kuangji.paopao.mapper.UserLabelMapper;
import com.kuangji.paopao.model.ConsultantModelService;
import com.kuangji.paopao.model.ConsultantSchedule;
import com.kuangji.paopao.model.MallGoods;
import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.model.UserLabel;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.service.ConsultantServiceService;
import com.kuangji.paopao.service.MallGoodsService;
import com.kuangji.paopao.service.SettledInService;
import com.kuangji.paopao.util.PropertiesFileUtil;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

//入驻
@Service
public class SettledInServiceImpl implements SettledInService {

    @Autowired
    private UserImageMapper userImageMapper;

    @Autowired
    private ConsultantMapper consultantMapper;

    @Autowired
    private MallGoodsMapper mallGoodsMapper;

    @Autowired
    private UserLabelMapper userLabelMapper;
    @Autowired
    private MallGoodsService mallGoodsService;

    private static final Long platformWorkingTime = Long
            .valueOf(PropertiesFileUtil.getInstance().get("platform_working_time"));
    @Autowired
    private ConsultantScheduleMapper consultantScheduleMapper;
    @Autowired
    PlatformWorkingTimeMapper platformWorkingTimeMapper;
    @Autowired
    private ConsultantServiceService consultantServiceService;

    // 入驻
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insertSetInDTO(SetInDTO dto, Integer userId) {

        List<UserLabel> userLabels = this.checkLabel(dto, userId);
        this.checkPlatformWorkingTimeStatus(dto.getPlatformWorkingTimeVOList());
        List<WorkDTO> workDTOs = this.checkWork(dto);
        mallGoodsMapper.delete(new MallGoods(userId, 0, 0));
        userLabelMapper.delete(new UserLabel(userId));
        userLabelMapper.insertBatch(userLabels);
        this.insertBatchConsultantSchedule(userId, dto.getPlatformWorkingTimeVOList());
        int consultationFee = workDTOs.stream().filter(c -> c.getServiceTimes() == 1).mapToInt(WorkDTO::getSellPrice)
                .min().getAsInt();
        if (consultationFee <= 0) {
            throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
        }
        Example example = new Example(Consultant.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        Consultant consultant = consultantMapper.selectOneByExample(example);
        consultant.setConsultationFee(consultationFee * 100);
        consultant.setStatus(ReviewStatusEnum.REVIEWING.getType());
        consultantMapper.updateByPrimaryKeySelective(consultant);
        List<MallGoods> mallGoodsList = this.mallGoodsList(workDTOs, userId, 0);
        mallGoodsMapper.insertBatch(mallGoodsList);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSetInDTO(SetInDTO dto, Integer userId) {
        int result = consultantServiceService.countConsultantService(userId);
        if (result > 0) {
            throw new ServiceException(ResultCodeEnum.SERVICE_VERIFY_WAITING);
        }
        List<UserLabel> userLabels = this.checkLabel(dto, userId);
        List<WorkDTO> workDTOs = this.checkWork(dto);
        int consultationFee = workDTOs.stream().filter(c -> c.getServiceTimes() == 1).mapToInt(WorkDTO::getSellPrice)
                .min().getAsInt();
        if (consultationFee <= 0) {
            throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);
        }
        userLabelMapper.delete(new UserLabel(userId));
        userLabelMapper.insertBatch(userLabels);
        List<ConsultantModelService> consultantModelServiceList = genConsultantServiceList(workDTOs, userId, dto.getRemoveWorks());
        if (CollectionUtils.isNotEmpty(consultantModelServiceList)) {
            consultantServiceService.insertBatch(consultantModelServiceList);
        }
    }

    private List<ConsultantModelService> genConsultantServiceList(List<WorkDTO> workDTOs, Integer userId, List<WorkDTO> removeWorkDTOs) {
        List<ConsultantModelService> consultantServiceList = new ArrayList<>();
        OperateTypeEnum operateTypeEnum;
        for (WorkDTO workDTO : workDTOs) {
            MallGoods mallGoods = transToMallGoods(workDTO, userId, 1);
            ConsultantModelService consultantModelService = new ConsultantModelService();
            if (workDTO.getGoodsId() > 0) {
                int count = mallGoodsService.countMallGoods(mallGoods);
                if (count > 0) {
                    continue;
                } else {
                    operateTypeEnum = OperateTypeEnum.UPDATE;
                }
            } else {
                operateTypeEnum = OperateTypeEnum.ADD;
            }
            BeanUtils.copyProperties(mallGoods, consultantModelService);
            consultantModelService.setGoodsId(mallGoods.getId());
            consultantModelService.setOperateType(operateTypeEnum.getType());
            consultantServiceList.add(consultantModelService);
        }
        for (WorkDTO workDTO : removeWorkDTOs) {
            MallGoods mallGoods = transToMallGoods(workDTO, userId, 1);
            ConsultantModelService consultantModelService = new ConsultantModelService();
            consultantModelService.setOperateType(OperateTypeEnum.DELETE.getType());
            BeanUtils.copyProperties(mallGoods, consultantModelService);
            consultantModelService.setGoodsId(mallGoods.getId());
            consultantServiceList.add(consultantModelService);
        }
        return consultantServiceList;
    }

    // 领域
    private List<UserLabel> checkLabel(SetInDTO dto, Integer userId) {
        List<UserLabel> labels = new ArrayList<>(8);
        Integer[] classIfication = dto.getClassification();
        for (Integer classIficationId : classIfication) {
            UserLabel userLabel = new UserLabel();
            userLabel.setLabelId(classIficationId);
            userLabel.setUserId(userId);
            userLabel.setUserLabelType(CommonEnum.INDEX_CLASSIFICATION_TYPE.getVal());
            labels.add(userLabel);
        }
        Integer[] contention = dto.getContention();
        for (Integer contentionId : contention) {
            UserLabel userLabel = new UserLabel();
            userLabel.setLabelId(contentionId);
            userLabel.setUserId(userId);
            userLabel.setUserLabelType(CommonEnum.CONTENTION_TYPE.getVal());
            labels.add(userLabel);
        }
        return labels;
    }

    // 领域
    private List<WorkDTO> checkWork(SetInDTO dto) {

        List<WorkDTO> workDTOs = dto.getWorks();
        // 验证入驻 工作模式
        if (workDTOs == null || workDTOs.isEmpty()) {
            throw new ServiceException(ResultCodeEnum.ERROR_SETTLED_IN_WORK);
        }
        int sellPrice = workDTOs.stream().mapToInt(WorkDTO::getSellPrice).min().getAsInt();
        if (sellPrice <= 0) {
            throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

        }
        int serviceTime = workDTOs.stream().mapToInt(WorkDTO::getServiceTimes).max().getAsInt();
        if (serviceTime > 24) {
            throw new ServiceException(ResultCodeEnum.ILLGEAL_PARAMTER);

        }
        return workDTOs;

    }

    // 生成商品
    private List<MallGoods> mallGoodsList(List<WorkDTO> workDTOs, Integer userId, Integer upDownFlag) {
        List<MallGoods> mallGoodsList = new ArrayList<>();
        for (WorkDTO workDTO : workDTOs) {
            mallGoodsList.add(transToMallGoods(workDTO, userId, upDownFlag));
        }
        return mallGoodsList;
    }

    private MallGoods transToMallGoods(WorkDTO workDTO, Integer userId, Integer upDownFlag) {
        Integer sellPrice = workDTO.getSellPrice();
        MallGoods mallGoods = new MallGoods();
        mallGoods.setId(workDTO.getGoodsId());
        mallGoods.setSellPrice(sellPrice * 100);
        mallGoods.setShopId(userId);
        mallGoods.setUpDownFlag(upDownFlag);
        if (workDTO.getServiceTimes() == 1) {
            mallGoods.setGoodsClass(MallGoodsClassEnum.CONSULTANT_SERVICE.getValue());
            mallGoods.setStock(1);
            mallGoods.setShareText(platformWorkingTime / 60 + CommonConstant.MINUTE);
            mallGoods.setGoodsName(sellPrice + CommonConstant.SECOND);
            mallGoods.setExpires(0);
        }
        if (workDTO.getServiceTimes() > 1) {
            mallGoods.setGoodsClass(MallGoodsClassEnum.SET_MEAL.getValue());
            mallGoods.setStock(workDTO.getServiceTimes());
            mallGoods.setGoodsName(sellPrice + CommonConstant.ZHANG);
            // 有效期
            mallGoods.setExpires(2*30 * workDTO.getServiceTimes());
            mallGoods.setShareText(
                    workDTO.getServiceTimes() + "*" + platformWorkingTime / 60 + CommonConstant.MINUTE);
        }
        mallGoods.setSellPointText(ConsultantWorkEnum.getConsultantWorkEnumValue(workDTO.getCode()));
        return mallGoods;
    }

    @Override
    public int deleteUserImg(Integer id, Integer userId) {
        UserImage userImage = new UserImage();
        userImage.setId(id);
        userImage.setUserId(userId);
        return userImageMapper.delete(userImage);
    }

    public int insertBatchConsultantSchedule(Integer userId, List<PlatformWorkingTimeVO> list) {
        consultantScheduleMapper.deleteByConsultantId(userId);
        for (PlatformWorkingTimeVO body : list) {
            ConsultantSchedule consultantSchedule = new ConsultantSchedule();
            StringBuffer unKey = new StringBuffer();
            unKey.append(userId)
                    .append("-")
                    .append(body.getConsultantWorkStart())
                    .append("-")
                    .append(body.getConsultantWorkEnd());
            consultantSchedule.setUnKey(unKey.toString());
            consultantSchedule.setStatus(body.getStatus());
            consultantSchedule.setPlatformWorkingTimeId(body.getPlatformWorkingTimeId());
            consultantSchedule.setScheduleStartTime(body.getConsultantWorkStart());
            consultantSchedule.setConsultantId(userId);
            consultantSchedule.setScheduleEndTime(body.getConsultantWorkEnd());
            consultantScheduleMapper.insertSelective(consultantSchedule);
        }

        return 1;
    }

    private void checkPlatformWorkingTimeStatus(List<PlatformWorkingTimeVO> list) {
        for (PlatformWorkingTimeVO body : list) {
            if (body.getStatus() == null || body.getStatus() == BookingTimeEnum.BOOKING.getValue()) {
                throw new ServiceException(ResultCodeEnum.ERROR_INVITATION_TIME_TYPE);
            }

        }
    }
}