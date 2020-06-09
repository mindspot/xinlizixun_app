package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.PointHistoryParam;
import com.kuangji.paopao.dto.result.PointHistoryResult;
import com.kuangji.paopao.service.PointHistoryService;
import com.kuangji.paopao.util.ServiceResultUtils;

@RestController
@RequestMapping("/point-history")
public class PointHistoryController {
    @Autowired
    private PointHistoryService pointHistoryService;
    @PostMapping("/list")
    public Object listPointHistoryResult(@RequestBody PointHistoryParam pointHistoryParam) {
        List<PointHistoryResult> list=pointHistoryService.listPointHistoryResult(pointHistoryParam);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }
}
