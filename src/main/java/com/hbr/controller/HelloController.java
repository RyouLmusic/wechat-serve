package com.hbr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: 汉高鼠刘邦
 * @Date: 2020/9/13 8:47
 */
@Controller
public class HelloController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello() {
        return "hello";
    }

    @RequestMapping("/hello/user")
    @ResponseBody
    public String user() {
        return "hello,User";
    }
}
