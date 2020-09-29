package com.bupt.lams.controller;

import com.bupt.lams.model.LamsUser;
import com.bupt.lams.service.HrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    HrService hrService;
    @GetMapping("/hrs")
    public List<LamsUser> getAllUsers() {
        return hrService.getAllHrsExceptCurrentHr();
    }
}
