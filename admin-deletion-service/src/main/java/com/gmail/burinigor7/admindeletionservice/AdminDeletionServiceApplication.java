package com.gmail.burinigor7.admindeletionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AdminDeletionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminDeletionServiceApplication.class, args);
    }

}
