package com.bupt.lams.controller;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    UserService userService;
    @GetMapping("/hrs")
    public List<LamsUser> getAllUsers() {
        return userService.getAllHrsExceptCurrentHr();
    }
}
