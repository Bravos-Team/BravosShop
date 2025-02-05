package com.bravos2k5.bravosshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BravosShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(BravosShopApplication.class, args);
    }

}
