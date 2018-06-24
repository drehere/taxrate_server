package com.bitmain.intelligent.tax;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bitmain.intelligent.tax.database")
public class RateTaxApplication {
    public static void main(String[] args){
        SpringApplication.run(RateTaxApplication.class,args);
    }
}
