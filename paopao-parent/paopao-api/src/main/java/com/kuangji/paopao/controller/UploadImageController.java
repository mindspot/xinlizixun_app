package com.kuangji.paopao.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import cn.hutool.http.HttpUtil;

/**
 * Author 金威正 Date 2020-02-20
 */
@RestController
@RequestMapping(value = "/v1/upload")
public class UploadImageController {

	private static final String IMG_PATH = "/images";

	private static final String SCENE = "images";
	
	private static final String OUT_PUT = "json";

	private static final long SIZE = 2 * 1024 * 1024;

	private static final String UPLOAD_PATH = "https://pp.psyooo.com/upload/group1/upload";

	@PostMapping(value = { "/img" })
	public Object list(MultipartFile file, HttpServletRequest request) {


		return "";
	}

	public String upload(File file) {
		Map<String, Object> params = new HashMap<>();
	    params.put("file",file);
		params.put("path", IMG_PATH);
		params.put("output", OUT_PUT);
		params.put("scene","images");
		String result = HttpUtil.post(UPLOAD_PATH, params);
		return result;
	}


}
