package com.benhouse.monitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by yunfeima on 2017/7/8.
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(MonitorApplication.class);
    }
    public static void main(String[] args) throws Exception {
        SpringApplication.run(MonitorApplication.class, args);
    }

}