package com.example.demo.filters;

import com.example.demo.v1.Movie;
import com.example.demo.v2.Title;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class V1MoviesToV2TitlesFilter implements  RewriteAndModifyResponseFilter<Title, Movie>{
    @Override
    public Class<Title> getSourceResponseClass() {
        return Title.class;
    }

    @Override
    public Class<Movie> getTargetResponseClass() {
        return Movie.class;
    }

    @Override
    public Function<Title, Movie> getMapper() {
        return (v2) ->
                Movie.builder()
                        .name(v2.getTitle())
                        .build();
    }

    @Override
    public String getSourcePathRegex() {
        return "/api/v1/movies";
    }

    @Override
    public String getTargetPathRegex() {
        return "/api/v2/titles";
    }

    @Override
    public String getId() {
        return "routeV1MoviesToV2Titles";
    }

    @Override
    public String getSourceHost() {
        return "*";
    }

    @Override
    public String getSourceFilterPath() {
        return "/api/v1/movies";
    }

    @Override
    public String getTargetUri() {
        return "http://service-2:8080";
    }
}
