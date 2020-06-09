package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Coupon;
import lombok.Data;

@Data
public class CouponResult extends Coupon {
    private User user;
}
