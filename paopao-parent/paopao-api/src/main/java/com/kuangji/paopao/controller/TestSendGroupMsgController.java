package com.kuangji.paopao.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kuangji.paopao.redis.AsyncTask;

@Controller
public class TestSendGroupMsgController {

	@Autowired
	private AsyncTask asyncTask;
	@ResponseBody
	@GetMapping(value = "/send")
	public Object send()  {
		asyncTask.sendGroupMsg(null, "你好久军军军军军军军军所撒奥 萨达方法阿萨达 ad发放", null);
		return "SUCCESS";
		
	}
	
}
