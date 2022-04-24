package com.example.shardingsphere_xa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

// @EnableTransactionManagement
@SpringBootApplication
public class ShardingsphereXaApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShardingsphereXaApplication.class, args);
    }

}
