package com.example.demo.filter.rewrite;

import com.example.demo.filter.service.ServiceFilterProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RewriteConfiguration {
    private final List<ModifyRequestRouteFilter<?,?>> modifyRequestRouteFilters;
    private final List<ModifyResponseRouteFilter<?,?>> modifyResponseRouteFilters;
    private final RewriteProperties rewriteProperties;
    private final ServiceFilterProperties serviceFilterProperties;
    public static final String BETA_ONLY_HEADER = "X-gateway-beta";


    @Bean
    public RouteLocator rewriteAndModifyResponseFilters(RouteLocatorBuilder builder) {
        var routes = builder.routes();
        var paths = rewriteProperties.getPaths();
        log.info("Found {} rewrite and modify response routes", paths.size());

        var requestMappingById = modifyRequestRouteFilters.stream()
                .collect(Collectors.toMap(ModifyRequestRouteFilter::getId, Function.identity()));
        var responseMappingById = modifyResponseRouteFilters.stream()
                .collect(Collectors.toMap(ModifyResponseRouteFilter::getId, Function.identity()));

        for (var key : paths.keySet()) {
            var filter = paths.get(key);
            log.info("ID: {}, Host: {}, Path: {}, Beta Only: {}. Allowed Methods: {}",
                    key,
                    filter.getRouteHost(),
                    filter.getRouteFilterPath(),
                    filter.isBetaOnly(),
                    filter.getAllowedMethods() == null ? "All" : filter.getAllowedMethods()
            );

            routes = routes.route(key, r -> {
                var route = r.host(filter.getRouteHost())
                        .and()
                        .path(filter.getRouteFilterPath());
                if (filter.isBetaOnly()) {
                    route = route.and().header(BETA_ONLY_HEADER, "true");
                }
                if(filter.getAllowedMethods() != null) {
                    var methods  = Arrays.stream(filter.getAllowedMethods()).map(methodType -> methodType.method).toList();
                    route = route.and().method( methods.toArray(new HttpMethod[]{}));
                }
                return route.filters((f) -> {
                            var updatedFilter = f.rewritePath(filter.getIncomingPathRegex(), filter.getTargetPathRegex());
                            if (responseMappingById.containsKey(key)) {
                                var responseFilter = (ModifyResponseRouteFilter<Object, Object>) responseMappingById.get(key);
                                if (responseFilter.getResponseMapper() != null) {
                                    log.debug("Added response mapping from {} to {}", responseFilter.getSourceResponseClass(), responseFilter.getOutgoingResponseClass());
                                    updatedFilter = updatedFilter.modifyResponseBody(
                                            responseFilter.getSourceResponseClass(),
                                            responseFilter.getOutgoingResponseClass(),
                                            (exc, v2) -> {

                                                var func = responseFilter.getResponseMapper();
                                                return Mono.justOrEmpty(
                                                        func.apply(v2)
                                                );
                                            }
                                    );
                                }
                            }
                            if (requestMappingById.containsKey(key)) {
                                var requestFilter = (ModifyRequestRouteFilter<Object, Object>) requestMappingById.get(key);
                                if (requestFilter.getRequestMapper() != null) {
                                    log.debug("Added request mapping from {} to {}", requestFilter.getIncomingRequestClass(), requestFilter.getSourceRequestClass());
                                    updatedFilter = updatedFilter.modifyRequestBody(
                                            requestFilter.getIncomingRequestClass(),
                                            requestFilter.getSourceRequestClass(),
                                            (exc, incoming) -> {
                                                var func = requestFilter.getRequestMapper();
                                                return Mono.justOrEmpty(
                                                        func.apply(incoming)
                                                );
                                            }
                                    );
                                }
                            }
                            return updatedFilter;
                        })
                        .uri(serviceFilterProperties.findUriByServiceId(filter.getSourceServiceId()));
            });

        }
        return routes.build();
    }

}
