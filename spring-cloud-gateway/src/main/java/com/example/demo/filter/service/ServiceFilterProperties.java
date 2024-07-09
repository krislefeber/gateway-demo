package com.example.demo.filter.service;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties("app.services")
public class ServiceFilterProperties {

    Map<String, RouteProperties> routes = new HashMap<>();

    public String findUriByServiceId(String serviceId) {
        return routes.get(serviceId).sourceUri;
    }

    @Value
    @Builder
    public static class RouteProperties {

        /**
         * The host name of the route to listen to.
         * To listen for all hosts, use "*"
         * example: *.example.com
         */
        String routeHost;

        /**
         * The route path to match against.
         * To listen for all paths, use "/**"
         * example: /my-app/v1/**
         */
        String routeFilterPath;

        /**
         * The host to direct the call to.
         * example: http://service-1:8080
         */
        String sourceUri;
    }
}
