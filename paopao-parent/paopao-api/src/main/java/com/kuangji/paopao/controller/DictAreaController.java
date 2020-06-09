package com.kuangji.paopao.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.service.DictAreaService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.DictAreaVO;

/**
 * Author 金威正
 * Date  2020-02-18
 */
@RestController
@RequestMapping(value = "/v1/dictArea")
public class DictAreaController {
    @Autowired
    private DictAreaService dictAreaService;

    /***
     * parentId 0 省
     * 
     * areaType 1直辖市  2市 3区
     * */
    @PostMapping(value = {"/list"})
    public Object list(Integer areaType,@RequestParam(defaultValue="0")Integer parentId) {
    
    	List<DictAreaVO> dictAreaVOs=dictAreaService.listDictAreaVO();
        return ServiceResultUtils.success(dictAreaVOs);
    }

    

}
