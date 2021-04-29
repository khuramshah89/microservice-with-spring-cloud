package com.microservice.api.account.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountsController {

    @GetMapping("/check")
    public String status(){
        return "Accounts working ....";
    }
}
