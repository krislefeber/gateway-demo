package com.example.demo.filters;

public interface RewritePathRouteFilter extends RouteFilter {

    String getSourcePathRegex();

    String getTargetPathRegex();
}
