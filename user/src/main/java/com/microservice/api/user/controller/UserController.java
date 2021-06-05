package com.microservice.api.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    Environment environment;
    @GetMapping("/check")
    public String getUser(){
        return "user controller working at "+environment.getProperty("local.server.port");
    }
}
