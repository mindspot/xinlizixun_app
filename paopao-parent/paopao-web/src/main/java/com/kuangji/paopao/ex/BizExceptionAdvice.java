package com.kuangji.paopao.ex;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kuangji.paopao.base.ServiceException;
import com.kuangji.paopao.util.ServiceResultUtils;

import lombok.extern.slf4j.Slf4j;
/**
 * 统一异常
 *
 * @author cosmo
 * @Title: CommonExceptionHandler
 * @ProjectName
 * @Description:
 * @date
 * 
 */
@RestControllerAdvice
@Slf4j
public class BizExceptionAdvice {

    /**
     * 全局异常捕捉处理
     *
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object errorHandler(HttpServletRequest request, Exception ex) {

        if (ex instanceof ServiceException) {
            ServiceException serviceException = (ServiceException) ex;
            return ServiceResultUtils.failure(String.valueOf(serviceException.getCode()),
                    serviceException.getMessage());
        }

        if (ex instanceof Exception) {
            log.error(ex.getMessage(), ex);
            return ServiceResultUtils.failure("-1", "未知错误请联系管理员");
        }
        return ex;
    }

    @ExceptionHandler(ServiceException.class)
    public Object serviceException(ServiceException ex) {
        return ServiceResultUtils.failure(ex.getCode()+"",ex.getMessage());
    }
}