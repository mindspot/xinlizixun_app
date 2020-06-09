package com.kuangji.paopao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.service.DispatchService;

/**
 * Author 金威正
 * Date  2020-02-17
 */
@RestController
public class DispatchController {
	
    @Autowired
    private DispatchService dispatchService;

}
