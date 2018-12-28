package com.sky.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello Controller.
 *
 * @author <a href="https://waylau.com">Way Lau</a>
 * @since 1.0.0 2017年11月20日
 */
@RestController
public class HelloController {

    //@RequestMapping("/hello")
    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}
