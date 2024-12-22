package com.senvu.train.batch;

import com.senvu.FeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(FeignConfig.class)
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class,args);
    }
}