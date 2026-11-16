package com.zhengsijin.collections;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhengsijin.collections.video.mapper")
public class CollectionsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CollectionsApplication.class, args);
    }
}
