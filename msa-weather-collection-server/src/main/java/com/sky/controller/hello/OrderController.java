package com.sky.controller.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OrderController {

    @RequestMapping("/getOrder")
    public Map<String, Object> getMember() {
        Map<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("errorCode", 200);
        hashMap.put("errorMsg", "订单项目");
        return hashMap;
    }

}
