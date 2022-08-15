package com.hangangnow.openapiserver;

import com.hangangnow.openapiserver.domain.hangangnow.HangangNowData;
import com.hangangnow.openapiserver.service.HangangNowService;
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
