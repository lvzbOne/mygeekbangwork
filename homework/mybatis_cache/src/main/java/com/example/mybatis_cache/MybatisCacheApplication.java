package com.example.mybatis_cache;

import com.example.mybatis_cache.service.BscDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class MybatisCacheApplication {

    @Autowired
    private BscDictService bscDictService;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MybatisCacheApplication.class, args);
//        Thread.sleep(10000);
//        new MybatisCacheApplication().bscDictService.info("aaa", 1);
    }

}
