package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.model.consultant.CouponTemplate;
import com.kuangji.paopao.model.consultant.PromotionalActivities;
import lombok.Data;

import java.util.List;

@Data
public class ActivitiesResult extends PromotionalActivities {
    private List<CouponTemplate> templateList;
}
