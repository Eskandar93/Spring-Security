package com.spring.springSecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/basic/")
public class BasicController {


    @GetMapping("mybasic")
    public String login(){
        return "My Name Is Alex";
    }

    @GetMapping("allbasic")
    public String all(){
        return "I am happy";
    }
}
