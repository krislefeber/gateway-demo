package com.example.demo.filters;

public interface RewritePathRouteFilter extends RouteFilter {

    String getSourcePathRegex();

    String getTargetPathRegex();

    /**
     * If true, then this will require a header of {@value com.example.demo.FilterConfiguration#BETA_ONLY_HEADER}
     * to be present with a value of "true"
     */
    default boolean betaOnly() {
        return false;
    }
}
