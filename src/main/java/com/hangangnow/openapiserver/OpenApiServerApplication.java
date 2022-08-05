package com.hangangnow.openapiserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class OpenApiServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OpenApiServerApplication.class, args);
    }

}
