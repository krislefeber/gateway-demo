package com.example.demo;

import com.example.demo.v1.Movie;
import com.example.demo.v2.Title;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }



//    @Bean
//    public RouteLocator routeV1MoviesToV2Titles(RouteLocatorBuilder builder) {
//
//        return builder.routes()
//                .route("routeV1MoviesToV2Titles", r -> r
//                        .path("/api/v1/movies")
//                        .and()
//                        .host()
//                        .filters((f) ->
//                                f.rewritePath("/api/v1/movies", "/api/v2/titles")
//                                        .modifyResponseBody(
//                                                Title.class,
//                                                Movie.class,
//                                                (exc, v2) -> Mono.justOrEmpty(
//                                                        Movie.builder()
//                                                                .name(v2.getTitle())
//                                                                .build()
//                                                )
//                                        )
//                        ).uri("http://service-2:8080"))
//                .build();
//    }

//
//    @Bean
//    public RouteLocator routeV1Service(RouteLocatorBuilder builder) {
//        return builder.routes()
//                .route("routeV1Service", r -> r
//                        .order(Ordered.LOWEST_PRECEDENCE)
//                        .path("/api/v1/**")
//                        .uri("http://service-1:8080"))
//                .build();
//    }
}
