package com.kuangji.paopao.mapper;

import java.util.List;

import com.kuangji.paopao.base.BaseMapper;
import com.kuangji.paopao.dto.param.PointHistoryParam;
import com.kuangji.paopao.dto.result.PointHistoryResult;
import com.kuangji.paopao.model.consultant.PointHistory;

/**
* Created by Mybatis Generator 2020/04/22
*/
public interface PointHistoryMapper extends BaseMapper<PointHistory> {
    List<PointHistoryResult> listPointHistoryResult(PointHistoryParam pointHistoryParam);
    
    List<PointHistory> listPointHistory(PointHistoryParam param);
}