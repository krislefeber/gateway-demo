package com.example.demo.filter.rewrite;

import java.util.function.Function;

public interface ModifyResponseRouteFilter<SourceResponse, OutgoingResponse> {

    String getId();

    Class<SourceResponse> getSourceResponseClass();

    Class<OutgoingResponse> getOutgoingResponseClass();

    Function<SourceResponse, OutgoingResponse> getResponseMapper();
}
