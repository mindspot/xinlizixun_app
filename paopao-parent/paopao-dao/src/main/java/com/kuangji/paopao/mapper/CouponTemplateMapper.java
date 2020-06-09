package com.kuangji.paopao.mapper;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.CouponTemplateParam;
import com.kuangji.paopao.model.consultant.CouponTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator 2020/05/18
*/
public interface CouponTemplateMapper extends BaseMapper<CouponTemplate> {
    List<CouponTemplate> listCouponTemplate(CouponTemplateParam couponTemplateParam);
    List<CouponTemplate> listByIds(String ids);
    int updateStatusByIds(@Param("ids") String ids, @Param("status") Integer status);
}