package com.example.demo.filters;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Service2GlobalFilter implements GlobalServiceFilter {
    @Override
    public String getId() {
        return "service-2-all";
    }

    @Override
    public String getSourceHost() {
        return "*";
    }

    @Override
    public String getSourceFilterPath() {
        return "/api/v2/**";
    }

    @Override
    public String getTargetUri() {
        return "http://service-2:8080";
    }
}
