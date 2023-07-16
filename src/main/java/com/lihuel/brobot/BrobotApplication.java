package com.lihuel.brobot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class BrobotApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrobotApplication.class, args);
    }

}
