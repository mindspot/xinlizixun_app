package com.framework.http.bean;

import java.util.HashMap;
import java.util.Map;


public abstract class RequestBean {

    //获取类名称
    private String bean_class_name;

    public RequestBean() {
        bean_class_name = this.getClass().getName();
    }


    public String getClassName() {
        return bean_class_name;
    }

    public abstract Object getTag(); //获取Volley请求标签

    public abstract Map<String, String> getHeaders();//获取请求header,包含cookie

    public abstract boolean isSetCache();//是否设置缓存

    public abstract HashMap<String, String> getParams();//获取
}
