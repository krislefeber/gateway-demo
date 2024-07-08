package com.example.demo.filter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ServiceFilterConfiguration {

    private final ServiceFilterProperties serviceFilterProperties;

    @Bean
    public RouteLocator globalServiceFilters(RouteLocatorBuilder builder) {
        var routes = builder.routes();
        var serviceFilters = serviceFilterProperties.routes;
        log.info("Found {} global routes", serviceFilters.size());

        for (var key : serviceFilters.keySet()) {
            var filter = serviceFilterProperties.routes.get(key);
            log.info("ID: {}, Host: {}, Path: {}",
                    key,
                    filter.getRouteHost(),
                    filter.getRouteFilterPath()
            );
            routes = routes.route(key, r -> r.order(Ordered.LOWEST_PRECEDENCE)
                    .host(filter.getRouteHost())
                    .and()
                    .path(filter.getRouteFilterPath())
                    .uri(filter.getSourceUri()));
        }
        return routes.build();
    }
}
