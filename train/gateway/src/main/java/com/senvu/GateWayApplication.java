package com.senvu;

import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class GateWayApplication {

    private static final Logger LOG = LoggerFactory.getLogger(GateWayApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GateWayApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("网关启动成功");
    }
}