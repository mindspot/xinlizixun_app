package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.model.UserImage;
import com.kuangji.paopao.service.UserImageService;
import com.kuangji.paopao.util.JwtUtils;
import com.kuangji.paopao.util.ServiceResultUtils;

import tk.mybatis.mapper.entity.Example;

/**
 * Author 金威正
 * Date  2020-02-20
 */
@RestController
@RequestMapping(value = "/v1/userImage")
public class UserImageController {
    @Autowired
    private UserImageService userImageService;
    @GetMapping(value = {"/list"})
    public Object list(Integer imgType,HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");
		Integer userId=JwtUtils.getUserId(token);
		Example example=new Example(UserImage.class);
		Example.Criteria criteria=example.createCriteria();
		criteria.andEqualTo("userId", userId);
		criteria.andEqualTo("imgType", imgType);
        List<UserImage> userImages = userImageService.findByExample(example);
        return ServiceResultUtils.success(userImages);
    }
    
    @PostMapping(value = {"/get"})
    public Object get(@RequestParam(required=true) int id) {
    
        UserImage userImage = userImageService.get(id);
        
        return ServiceResultUtils.success(userImage);
    }

    @PostMapping(value = "/insert")
    public Object insert(UserImage userImage) {
        if (userImageService.insert(userImage) > 0) {
            return ServiceResultUtils.success(null);
        } else {
            return ServiceResultUtils.failure("-1");
        }
    }

  
    @PostMapping(value = "/update")
    public Object update(UserImage userImage) {
        if (userImageService.updateByPrimaryKeySelective(userImage) > 0) {
          return ServiceResultUtils.success(null);
        } else {
            return ServiceResultUtils.failure("-1");
        }
    }

    @PostMapping(value = "/delete")
    public Object delete(int id) {
        if (userImageService.logicDeleteById(id) > 0) {
        	return ServiceResultUtils.success(null);
        } else {
            return ServiceResultUtils.failure("-1");
        }
    }

}
