package com.example.bootredis.controller;

import com.example.bootredis.service.BloomFilterService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class BloomFilterController {

    @Resource
    private BloomFilterService bloomFilterService;

    @RequestMapping("/bloom/idExists")
    public boolean ifExists(int id){
        return bloomFilterService.userIdExists(id);
    }

}
