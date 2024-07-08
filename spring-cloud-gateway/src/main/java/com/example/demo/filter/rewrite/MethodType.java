package com.example.demo.filter.rewrite;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;

@RequiredArgsConstructor
public enum MethodType {
    GET(HttpMethod.GET),
    POST(HttpMethod.POST),
    PUT(HttpMethod.PUT),
    DELETE(HttpMethod.DELETE),
    ;

    public final HttpMethod method;
}
