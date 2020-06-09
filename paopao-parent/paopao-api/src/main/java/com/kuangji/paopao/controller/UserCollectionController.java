package com.kuangji.paopao.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kuangji.paopao.base.Pager;
import com.kuangji.paopao.service.UserCollectionService;
import com.kuangji.paopao.service.UserService;
import com.kuangji.paopao.util.ServiceResultUtils;
import com.kuangji.paopao.vo.ConsultantVO;

/**
 * Author 金威正 Date 2020-02-22
 */
@RestController
@RequestMapping(value = "/v1/collection")
public class UserCollectionController {
	@Autowired
	private UserCollectionService collectionService;

	@Autowired
	private UserService userService;

	// 获取收藏夹list
	@GetMapping("/list/{pageNum}")
	public Object list(@PathVariable("pageNum")  int pageNum, HttpServletRequest request) {
		if (pageNum <= 0) {
			return ServiceResultUtils.failure("-1", "跳转页码最小为1");

		}
		String token = request.getHeader("authorization").replace("Bearer ", "");

		Pager<List<ConsultantVO>> pageInfo = userService.pageListCollectionConsultant(token, pageNum);

		return ServiceResultUtils.success(pageInfo);
	}

	@PostMapping(value = "/insert")
	public Object insert(@RequestParam int collectionId, HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");

		if (collectionService.insert(token, collectionId) > 0) {
			return ServiceResultUtils.success(null);
		} else {
			return ServiceResultUtils.failure("-1");
		}
	}
	
	@DeleteMapping(value = "/{collectionId}")
	public Object delete(@PathVariable("collectionId") int collectionId, HttpServletRequest request) {
		String token = request.getHeader("authorization").replace("Bearer ", "");

		if (collectionService.deleteCollection(token, collectionId) > 0) {
			return ServiceResultUtils.success(null);
		} else {
			return ServiceResultUtils.failure("-1");
		}
	}
}
