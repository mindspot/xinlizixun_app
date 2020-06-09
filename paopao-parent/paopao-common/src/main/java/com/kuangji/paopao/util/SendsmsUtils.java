package com.kuangji.paopao.util;

import java.io.BufferedReader;

//接口类型：互亿无线触发短信接口，支持发送验证码短信、订单通知短信等。
//账户注册：请通过该地址开通账户http://user.ihuyi.com/register.html
//注意事项：
//（1）调试期间，请使用用系统默认的短信内容：您的验证码是：【变量】。请不要把验证码泄露给其他人。
//（2）请使用 APIID 及 APIKEY来调用接口，可在会员中心获取；
//（3）该代码仅供接入互亿无线短信接口参考使用，客户可根据实际需要自行编写；

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.kuangji.paopao.base.ResultCodeEnum;
import com.kuangji.paopao.base.ServiceException;

public class SendsmsUtils {

	public static String send(String postUrl,int code, String uid, String pw, String mo,String appName) {


		String content = new String(appName+"验证码是：" + code + "。请不要把验证码泄露给其他人。");

		try {
			URL url = new URL(postUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);// 允许连接提交信息
			connection.setRequestMethod("POST");// 网页提交方式"GET"、"POST"
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Connection", "Keep-Alive");
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String tm=dateFormat.format(new Date());
			StringBuffer sb = new StringBuffer();
			sb.append("uid=" + uid).
					append("&pw=" + MD5Utils.stringToMD5(pw+tm))
					.append("&mb=" + mo)
					.append("&ms=" +content)
					.append("&tm=" + tm);

			OutputStream os = connection.getOutputStream();
			os.write(sb.toString().getBytes());
			os.close();
			String line, result = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			in.close();
			return result;
		} catch (IOException e) {

		}
		throw new ServiceException(ResultCodeEnum.FAIL);

	}

	
	
	public static String sendStartOrder(String postUrl, String uid, String pw, String mo,String appName,String content) {


		content = new String(appName+content);

		try {
			URL url = new URL(postUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);// 允许连接提交信息
			connection.setRequestMethod("POST");// 网页提交方式"GET"、"POST"
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("Connection", "Keep-Alive");
			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String tm=dateFormat.format(new Date());
			StringBuffer sb = new StringBuffer();
			sb.append("uid=" + uid).
					append("&pw=" + MD5Utils.stringToMD5(pw+tm))
					.append("&mb=" + mo)
					.append("&ms=" +content)
					.append("&tm=" + tm);

			OutputStream os = connection.getOutputStream();
			os.write(sb.toString().getBytes());
			os.close();
			String line, result = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
			while ((line = in.readLine()) != null) {
				result += line + "\n";
			}
			in.close();
			return result;
		} catch (IOException e) {

		}
		throw new ServiceException(ResultCodeEnum.FAIL);

	}
}
