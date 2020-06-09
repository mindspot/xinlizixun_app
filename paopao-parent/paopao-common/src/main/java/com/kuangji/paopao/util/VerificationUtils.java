package com.kuangji.paopao.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VerificationUtils {

	
	public static boolean isMobilePhone(String value) {
			
          String s2="^[1](([3|5|8][\\d])|([4][4,5,6,7,8,9])|([6][2,5,6,7])|([7][^9])|([9][1,8,9]))[\\d]{8}$";// 验证手机号
		  Pattern p = Pattern.compile(s2);
	      Matcher m = p.matcher(value);
	      return m.matches();
    }
	
	
	public static boolean isEmail(String value) {
        String emailPattern = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(emailPattern);
        Matcher m = p.matcher(value);
        return m.matches();
    }
}
