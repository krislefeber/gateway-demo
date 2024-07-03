package com.example.demo.filters;

import com.example.demo.v1.Movie;
import com.example.demo.v2.Title;

public interface RewriteAndModifyResponseFilter<SourceResponse, TargetResponse> extends ModifyResponseRouteFilter<SourceResponse, TargetResponse>, RewritePathRouteFilter {
}
