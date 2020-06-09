package com.kuangji.paopao.util;

import java.io.Serializable;

import com.kuangji.paopao.base.ResultCodeEnum;

/**
 * **************************************************************
 * 金威正
 */
public class ServiceResultUtils<T> implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7594073105833045644L;

	private boolean success = false;

    private String code;

    private String message;

    private T result;

    private ServiceResultUtils() {
    }

    public static <T> ServiceResultUtils<T> success(T result) {
        ServiceResultUtils<T> item = new ServiceResultUtils<T>();
        item.success = true;
        item.result = result;
        item.code = "0";
        item.message = "操作成功";
        return item;
    }

    @SuppressWarnings("unchecked")
	public static <T> ServiceResultUtils<T> failure(T result) {
        ServiceResultUtils<T> item = new ServiceResultUtils<T>();

        if (result instanceof ResultCodeEnum) {
			  item.success = false;
			  item.result = result;
			  item.code = "-1";
			  ResultCodeEnum codeEnum= (ResultCodeEnum) result;
			  item.code = String.valueOf(codeEnum.getCode());
			  item.result = (T) codeEnum.getMsg();
			  item.message = "failure";
			  return item;
		}else{
            item.success = false;
            item.code = "-1";
            item.result = result;
            item.message = "failure";
        }
		return item;
       
        
    }
    public static <T> ServiceResultUtils<T> failure(String errorCode, String errorMessage) {
        ServiceResultUtils<T> item = new ServiceResultUtils<T>();
        item.success = false;
        item.code = errorCode;
        item.message = errorMessage;
        return item;
    }

    
    public static <T> ServiceResultUtils<T> failure(String errorCode, String errorMessage,T resul) {
        ServiceResultUtils<T> item = new ServiceResultUtils<T>();
        item.success = false;
        item.code = errorCode;
        item.message = errorMessage;
        item.result=resul;
        return item;
    }
    
    public static <T> ServiceResultUtils<T> failure(String errorCode) {
        ServiceResultUtils<T> item = new ServiceResultUtils<T>();
        item.success = false;
        item.code = "-1";
        item.message = "failure";
        return item;
    }

    public boolean hasResult() {
        return result != null;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getResult() {
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
