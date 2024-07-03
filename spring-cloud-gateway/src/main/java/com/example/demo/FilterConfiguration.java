package com.example.demo;

import com.example.demo.filters.GlobalServiceFilter;
import com.example.demo.filters.RewriteAndModifyResponseFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FilterConfiguration {

    private final List<GlobalServiceFilter> globalServiceFilters;
    private final List<RewriteAndModifyResponseFilter<?, ?>> rewriteAndModifyResponseFilters;

    @Bean
    public RouteLocator globalServiceFilters(RouteLocatorBuilder builder) {
        var routes = builder.routes();
        log.info("Found {} global routes", globalServiceFilters.size());

        for (var filter : globalServiceFilters) {
            routes = routes.route(filter.getId(), r -> r.order(Ordered.LOWEST_PRECEDENCE)
                    .host(filter.getSourceHost())
                    .and()
                    .path(filter.getSourceFilterPath())
                    .uri(filter.getTargetUri()));

        }
        return routes.build();
    }


    @Bean
    public RouteLocator rewriteAndModifyResponseFilters(RouteLocatorBuilder builder) {
        var routes = builder.routes();
        log.info("Found {} rewrite and modify response routes", rewriteAndModifyResponseFilters.size());

        for (var service : rewriteAndModifyResponseFilters) {
            /**
             * In order to get around spring's limitations on dependency injection
             * with lists, we can map the wildcard(?) to an Object here, which allows
             * the mapping function below to work properly.
             */
            var filter = (RewriteAndModifyResponseFilter<Object, Object>)service;
            routes = routes.route(service.getId(), r ->
                    r.order(Ordered.LOWEST_PRECEDENCE)
                            .host(service.getSourceHost())
                            .and()
                            .path(service.getSourceFilterPath())
                            .filters((f) ->
                                    f.rewritePath(filter.getSourcePathRegex(), filter.getTargetPathRegex())
                                            .modifyResponseBody(
                                                    filter.getSourceResponseClass(),
                                                    filter.getTargetResponseClass(),
                                                    (exc, v2) -> {

                                                        var func = filter.getMapper();
                                                        return Mono.justOrEmpty(
                                                                func.apply(v2)
                                                        );
                                                    }
                                            )
                            )
                            .uri(filter.getTargetUri()));

        }
        return routes.build();
    }
}
