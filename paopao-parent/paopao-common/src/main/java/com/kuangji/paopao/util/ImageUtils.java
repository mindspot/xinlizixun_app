package com.kuangji.paopao.util;



public class ImageUtils {

	
	public static String getImgagePath(String imageUrl,String path) {
		
		return new StringBuffer(imageUrl).append(path).toString();
	
	}
	
}
