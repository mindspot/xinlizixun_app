package com.kuangji.paopao.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
/**
 * Author 金威正
 * Date  2020-02-27
 */
@Data
public class MapBookingTimeVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String  week;
 
    private List<BookingTimeVO> list;
    
    
    private  boolean stopBooking;
}