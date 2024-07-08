package com.example.demo.filter.rewrite;

import com.example.demo.v1.Movie;
import com.example.demo.v2.Title;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class V1MoviesToV2TitlesResponseFilter implements ModifyResponseRouteFilter<Title, Movie> {

    @Override
    public String getId() {
        return "routeV1MoviesToV2Titles";
    }

    @Override
    public Class<Title> getSourceResponseClass() {
        return Title.class;
    }

    @Override
    public Class<Movie> getOutgoingResponseClass() {
        return Movie.class;
    }

    @Override
    public Function<Title, Movie> getResponseMapper() {
        return (v2) ->
                Movie.builder()
                        .name(v2.getTitle())
                        .build();
    }
}
