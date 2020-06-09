package com.kuangji.paopao.mapper;

import com.kuangji.paopao.model.consultant.ReviewRecord;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

/**
* Created by Mybatis Generator 2020/04/11
*/
public interface ReviewRecordMapper extends Mapper<ReviewRecord> {
    ReviewRecord getLastReviewRecord(@Param("refId") Integer refId, @Param("refType") Integer refType);
}