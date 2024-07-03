package com.example.demo;

import com.example.demo.v1.Movie;
import com.example.demo.v2.Title;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder) {

		return builder.routes()
				.route("v2", r -> r
						.path("/api/v1/movies")
						.filters((f) ->
							f.rewritePath("/api/v1/movies", "/api/v2/titles")
									.modifyResponseBody(
											Title.class,
											Movie.class,
											(exc, v2) -> Mono.justOrEmpty(
													Movie.builder()
													.name(v2.getTitle())
													.build()
											)
									)
						).uri("http://service-2:8080"))
				.build();
	}
}
