package com.sky.err;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice("com.sky.controller")
public class GlobalExceptionHandler {

    // 拦截json格式
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Map<String, Object> JSONErrorHandler() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("errorCode", "500");
        map.put("errorMsg", "系统错误!");
        return map;
    }

}
