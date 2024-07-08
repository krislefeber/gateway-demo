package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.wiremock.integrations.testcontainers.WireMockContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class CallServiceTest {

    @Autowired
    private WebTestClient webClient;

    @Container
    static WireMockContainer service1 = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withFileFromResource(
                    "wiremock/service-1/mappings/get-movies.json")
            .withFileFromResource("wiremock/service-1/mappings/get-series.json");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.services.routes.service-1.source-uri", service1::getBaseUrl);
    }

    @Test
    public void shouldRoute() {
        // TODO: run this once you have docker setup.
        webClient.get().uri("/api/v1/movies")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                      {
                        "name": "I'm version 1"
                      }
                """);
    }
}
