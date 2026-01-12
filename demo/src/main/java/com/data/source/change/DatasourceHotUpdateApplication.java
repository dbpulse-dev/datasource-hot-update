package com.data.source.change;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.data.source"})
public class DatasourceHotUpdateApplication {
    private static final Logger logger = LoggerFactory.getLogger(DatasourceHotUpdateApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(DatasourceHotUpdateApplication.class, args);
        logger.info("Starting TestApplication successfully...");
    }

}
