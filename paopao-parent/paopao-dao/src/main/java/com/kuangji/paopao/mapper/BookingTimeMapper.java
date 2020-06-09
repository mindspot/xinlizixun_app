package com.kuangji.paopao.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.model.BookingTime;

/**
* Created by Mybatis Generator 2020/03/14
*/
public interface BookingTimeMapper extends BaseMapper<BookingTime> {


    /**
     * 根据传入数参数获取数据
     * Author 金威正
     * Date  2020-02-27
     */
     List<Integer> listUserBookingTime(Integer userId);




    /**
     * 根据传入咨询师id找
     * Author 金威正
     * Date  2020-02-27
     */
     List<BookingTime> listConsultantBookingTime(int consultantId);
     
     /**
      * 根据用户id 删除
      * Author 金威正
      * Date  2020-02-27
      */
     
     int   deleteByConsultantId(@Param("consultantId")Integer userId);
     
     
     List<Integer>  listUseBookingId(@Param("consultantId")Integer consultantId);
     

     
     Date  getUserMaxDate(@Param("userId")Integer userId);
     
     
     
}