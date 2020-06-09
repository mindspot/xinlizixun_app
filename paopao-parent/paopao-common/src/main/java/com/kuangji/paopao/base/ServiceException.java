package com.kuangji.paopao.base;
/**
 * 业务异常，业务校验不通过，或操作无法执行时触发<br>
 * 应将 SQLException 等消费方无法处理的异常包装成该异常抛出，消费方将各自处理本异常
 * @author haolong
 */
public class ServiceException extends RuntimeException{
	/** 验证版本一致性 */
    private static final long serialVersionUID = 1L;

    /** 业务异常编码 */
    private int code;

    public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}


    public ServiceException(ResultCodeEnum resultCodeEnum) {
    	super(resultCodeEnum.getMsg());
		this.code = resultCodeEnum.getCode();
	}

	/** 获取业务异常编码 */
	public int getCode() {
		return code;
	}

}
