package com.example.demo;

import com.example.demo.filter.rewrite.RewriteConfiguration;
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
            .withMappingFromResource(
                    "wiremock/service-1/mappings/get-movies.json")
            .withMappingFromResource(
                    "wiremock/service-1/mappings/get-series.json");

    @Container
    static WireMockContainer service2 = new WireMockContainer("wiremock/wiremock:3.6.0")
            .withMappingFromResource(
                    "wiremock/service-2/mappings/get-titles.json");


    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.services.routes.service-1.source-uri", service1::getBaseUrl);
        registry.add("app.services.routes.service-2.source-uri", service2::getBaseUrl);
    }

    @Test
    public void shouldRouteToService1() {
        webClient.get().uri("/api/v1/movies")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                      {
                        "name": "I'm version 1"
                      }
                      """);
        webClient.get().uri("/api/v1/series")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                     {
                           "name": "I'm version 1 series"
                     }
                     """);
    }

    @Test
    public void shouldRouteToService2() {
        webClient.get().uri("/api/v2/titles")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                      {
                        "title": "I'm version 2"
                      }
                      """);
    }

    @Test
    public void shouldRouteToService2WithBetaFlag() {
        webClient.get().uri("/api/v1/movies")
                .header(RewriteConfiguration.BETA_ONLY_HEADER, "true")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .json("""
                      {
                        "name": "I'm version 2"
                      }
                      """);
    }
}
