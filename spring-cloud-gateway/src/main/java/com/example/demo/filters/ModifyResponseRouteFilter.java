package com.example.demo.filters;

import java.util.function.Function;

public interface ModifyResponseRouteFilter<SourceResponse, TargetResponse> extends RouteFilter {

    Class<SourceResponse> getSourceResponseClass();

    Class<TargetResponse> getTargetResponseClass();

    Function<SourceResponse, TargetResponse> getMapper();
}
