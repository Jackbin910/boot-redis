package com.example.bootredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BootRedisApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootRedisApplication.class, args);
    }

}
