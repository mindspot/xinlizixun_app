package com.kuangji.paopao.controller;

import com.github.pagehelper.PageInfo;
import com.kuangji.paopao.dto.param.PromotionalActivitiesParam;
import com.kuangji.paopao.dto.result.ActivitiesResult;
import com.kuangji.paopao.model.consultant.PromotionalActivities;
import com.kuangji.paopao.service.PromotionalActivitiesService;
import com.kuangji.paopao.util.ServiceResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotional-activities")
public class PromotionalActivitiesController {
    @Autowired
    private PromotionalActivitiesService promotionalActivitiesService;

    @PostMapping(value = {"/list"})
    public Object listCoupon(@RequestBody PromotionalActivitiesParam param) {
        List<ActivitiesResult> list = promotionalActivitiesService.listPromotionalActivities(param);
        return ServiceResultUtils.success(new PageInfo<>(list));
    }

    @PostMapping(value = {"/add"})
    public Object addPromotionalActivities(@RequestBody PromotionalActivities promotionalActivities) {
        return ServiceResultUtils.success(promotionalActivitiesService.addPromotionalActivities(promotionalActivities) > 0);
    }

    @PostMapping(value = {"/update"})
    public Object updatePromotionalActivities(@RequestBody PromotionalActivities promotionalActivities) {
        return ServiceResultUtils.success(promotionalActivitiesService.updatePromotionalActivities(promotionalActivities) > 0);
    }

    @GetMapping("/get/{id}")
    public Object getPromotionalActivities(@PathVariable Integer id) {
        PromotionalActivities promotionalActivities = promotionalActivitiesService.findById(id);
        return ServiceResultUtils.success(promotionalActivities);
    }
}
