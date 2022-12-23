package com.jokerxin.x509ca;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jokerxin.x509ca.mapper")
public class X509CaApplication {

    public static void main(String[] args) {
        SpringApplication.run(X509CaApplication.class, args);
    }

}
