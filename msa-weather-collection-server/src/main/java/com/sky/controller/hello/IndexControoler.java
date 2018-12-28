package com.sky.controller.hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexControoler {

    @RequestMapping("/index")
    public String result() {
        return "这是我的第一个springboot2.0项目";
    }

    public static void main(String[] args) {
        System.out.println("sdf");
    }

}
