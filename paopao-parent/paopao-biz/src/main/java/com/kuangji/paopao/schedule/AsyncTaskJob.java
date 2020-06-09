package com.kuangji.paopao.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.kuangji.paopao.mapper.BookingTimeMapper;
import com.kuangji.paopao.mapper.ConsultantMapper;
import com.kuangji.paopao.mapper.MallTradeOrderMapper;
import com.kuangji.paopao.mapper.PlatformWorkingTimeMapper;
import com.kuangji.paopao.mapper.PointHistoryMapper;
import com.kuangji.paopao.mapper.SubCommissionRecordMapper;
import com.kuangji.paopao.mapper.UserMapper;
import com.kuangji.paopao.model.BookingTime;
import com.kuangji.paopao.model.MallTradeOrder;
import com.kuangji.paopao.model.Visitor;
import com.kuangji.paopao.model.consultant.ConsultantRate;
import com.kuangji.paopao.model.consultant.SubCommissionRecord;
import com.kuangji.paopao.service.ConsultantService;
import com.kuangji.paopao.service.MallTradeOrderService;
import com.kuangji.paopao.service.VisitorService;
import com.kuangji.paopao.vo.PlatformWorkingTimeVO;

@Component
@Async
public class AsyncTaskJob {
    @Autowired
    private BookingTimeMapper bookingTimeMapper;
    @Autowired
    private PlatformWorkingTimeMapper platformWorkingTimeMapper;
    @Autowired
    private SubCommissionRecordMapper subCommissionRecordMapper;
    @Autowired
    private ConsultantService consultantService;
    @Autowired
    private MallTradeOrderService mallTradeOrderService;
    @Autowired
    private VisitorService visitorService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ConsultantMapper consultantMapper;
    
    @Autowired
    private PointHistoryMapper pointHistoryMapper;
    
    @Value("${platform.invitation-code}")
    private String PlatformInvitationCode;
    
    @Autowired
    private MallTradeOrderMapper mallTradeOrderMapper;
    
    public Future<String> genBookingTask(Integer userId) {
        List<PlatformWorkingTimeVO> list = platformWorkingTimeMapper.listWorkingUserTime(userId);
        String[] calendars = new String[8];
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<BookingTime> bookingTimes = new ArrayList<>();
        for (PlatformWorkingTimeVO body : list) {
            BookingTime bookingTime = new BookingTime();
            bookingTime.setConsultantId(userId);
//			bookingTime.setSortTime(DateUtils.formatSecond(body.getScheduleEndTime()));
//			bookingTime.setCode(DateUtils.bookTimeCode(body.getConsultantWorkStartTime()));
            bookingTime.setConsultantWorkStartTime(body.getConsultantWorkStart());
            bookingTime.setConsultantWorkEndTime(body.getConsultantWorkEnd());
            bookingTime.setTimeType(body.getStatus());
            bookingTimes.add(bookingTime);
        }

        for (int i = 0; i < calendars.length; i++) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            calendar.add(Calendar.DATE, i);
            calendars[i] = sdf.format(calendar.getTime());
            String date = calendars[i];
            for (BookingTime bookingTime : bookingTimes) {
                StringBuffer unkey = new StringBuffer();
                unkey.append(userId).append("-").append(date).append("-")
                        .append(bookingTime.getConsultantWorkStartTime());
                bookingTime.setUnKey(unkey.toString());
                bookingTime.setConsultantWorkDate(date);
                bookingTime.setId(null);
                bookingTimeMapper.insertSelective(bookingTime);
            }
        }
        return new AsyncResult<>(userId + "预定记录同步成功");
    }

    public Future<String> genSubCommissionRecord(MallTradeOrder mallTradeOrder) {
        Integer orderType;
        Boolean result = mallTradeOrderService.checkIfFirstOrder(mallTradeOrder);
        if (result) {
            orderType = 1;
        } else {
            orderType = 2;
        }
        ConsultantRate consultantRate = consultantService.getConsultantRate(mallTradeOrder.getShopId(), orderType);
        SubCommissionRecord subCommissionRecord = new SubCommissionRecord();
        subCommissionRecord.setOrderId(mallTradeOrder.getId());
        subCommissionRecord.setMemberUserId(mallTradeOrder.getBuyerId());
        subCommissionRecord.setConsultantUserId(mallTradeOrder.getShopId());
        subCommissionRecord.setTotalAmount(mallTradeOrder.getOrderAmount());
        subCommissionRecord.setPlatformRate(consultantRate.getPlatformRate());
        subCommissionRecord.setPlatformAmount(consultantRate.getPlatformRate() * mallTradeOrder.getOrderAmount()/100);
        subCommissionRecord.setChannelRate(consultantRate.getChannelRate());
        subCommissionRecord.setChannelAmount(consultantRate.getChannelRate() * mallTradeOrder.getOrderAmount()/100);
        subCommissionRecord.setConsultantRate(consultantRate.getConsultantRate());
        subCommissionRecord.setConsultantAmount(consultantRate.getConsultantRate() * mallTradeOrder.getOrderAmount()/100);
        subCommissionRecord.setPartnerRate(consultantRate.getPartnerRate());
        subCommissionRecord.setPartnerAmount(consultantRate.getPartnerRate() * mallTradeOrder.getOrderAmount()/100);
        subCommissionRecordMapper.insertSelective(subCommissionRecord);
        return new AsyncResult<>(mallTradeOrder.getOrderNo() + "记录同步成功");
    }


    public Future<Integer> insertVisitor(Visitor visitor) {
    
    	
		return new  AsyncResult<>(visitorService.insertVisitor(visitor));
    }
    
  
    
   
}
