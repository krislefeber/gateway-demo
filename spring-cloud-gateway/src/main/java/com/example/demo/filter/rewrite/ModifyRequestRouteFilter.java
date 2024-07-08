package com.example.demo.filter.rewrite;

import java.util.function.Function;

public interface ModifyRequestRouteFilter<IncomingRequest, SourceRequest> {

    String getId();

    Class<IncomingRequest> getIncomingRequestClass();

    Class<SourceRequest> getSourceRequestClass();

    Function<IncomingRequest, SourceRequest> getRequestMapper();
}
