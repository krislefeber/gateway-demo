package com.example.demo.filters;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Service1GlobalFilter implements GlobalServiceFilter {
    @Override
    public String getId() {
        return "service-1-all";
    }

    @Override
    public String getSourceHost() {
        return "*";
    }

    @Override
    public String getSourceFilterPath() {
        return "/api/v1/**";
    }

    @Override
    public String getTargetUri() {
        return "http://service-1:8080";
    }
}
