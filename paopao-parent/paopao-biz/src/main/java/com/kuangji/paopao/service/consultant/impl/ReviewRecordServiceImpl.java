package com.kuangji.paopao.service.consultant.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kuangji.paopao.enums.ReviewStatusEnum;
import com.kuangji.paopao.enums.UserTypeEnum;
import com.kuangji.paopao.mapper.ReviewRecordMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.ReviewRecord;
import com.kuangji.paopao.redis.Easemob;
import com.kuangji.paopao.schedule.AsyncTaskJob;
import com.kuangji.paopao.service.ConsultantService;
import com.kuangji.paopao.service.MallGoodsService;
import com.kuangji.paopao.service.consultant.ReviewRecordService;
import com.kuangji.paopao.util.PropertiesFileUtil;

@Service
public class ReviewRecordServiceImpl implements ReviewRecordService {
    @Autowired
    private ReviewRecordMapper reviewRecordMapper;
    @Autowired
    private ConsultantService consultantService;
    @Autowired
    private MallGoodsService mallGoodsService;
    @Autowired
    private AsyncTaskJob asyncTask;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Easemob easemob;
    public static final String EASEMOB_CONSULTANT = PropertiesFileUtil.getInstance().get("easemob_consultant");
    public static final String EASEMOB_PWD = PropertiesFileUtil.getInstance().get("easemob_pwd");
    //推广的url生成
    @Value("${platform.spread-url}")
    private String SPREAD_URL;

    @Override
    public int addReview(ReviewRecord reviewRecord) {
        switch (UserTypeEnum.getUserTypeEnum(reviewRecord.getRefType())) {
            case CONSULTANT:
                Consultant consultant = new Consultant();
                consultant.setStatus(reviewRecord.getStatus());
                consultant.setId(reviewRecord.getRefId());
                String spreadUrl = SPREAD_URL + consultant.getId();
                consultant.setSpreadUrl(spreadUrl);
                consultantService.updateByPrimaryKeySelective(consultant);
                if (reviewRecord.getStatus() == ReviewStatusEnum.PASSED.getType()) {
                    // 环信逻辑
                    Consultant consultant1 = consultantService.findById(reviewRecord.getRefId());
                    easemob.createUser(EASEMOB_CONSULTANT + consultant1.getUserId(), EASEMOB_PWD);
                    Integer userId = consultant1.getUserId();
                    User user = new User();
                    user.setId(userId);
                    user.setEasemobId(EASEMOB_CONSULTANT + consultant1.getUserId());
                    userMapper.updateByPrimaryKeySelective(user);
                    // 上架逻辑
                    mallGoodsService.doConsultantOnSale(userId);
                    // 预约逻辑
                    asyncTask.genBookingTask(userId);
                }

                break;
        }
        return reviewRecordMapper.insertSelective(reviewRecord);
    }

    @Override
    public ReviewRecord getLastReviewRecord(Integer refId, Integer refType) {
        return reviewRecordMapper.getLastReviewRecord(refId, refType);
    }
}
