package com.example.demo.filter.rewrite;

import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Data
@Configuration
@ConfigurationProperties("app.rewrites")
public class RewriteProperties {
    Map<String, RewritePath> paths = new HashMap<>();

    @Value
    @Builder
    public static class RewritePath {
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
         * The id of the service to direct the call to.
         * example: service-2
         */
        String sourceServiceId;
        String incomingPathRegex;
        String targetPathRegex;

        /**
         * If true, then this will require a header of {@value RewriteProperties#BETA_ONLY_HEADER}
         * to be present with a value of "true"
         */
        boolean betaOnly;

        /**
         * Returns the list of allowed HTTP methods.
         * If null is returned, all methods will be matched
         * example: [HttpMethod.GET, HttpMethod.POST]
         */
        MethodType[] allowedMethods;
    }

}
