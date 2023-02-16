package com.skillup;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SkillUpApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkillUpApplication.class, args);
    }
}