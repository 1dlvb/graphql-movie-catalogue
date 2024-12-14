package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @QueryMapping
    public List<Movie> movies() {
        return movieService.getAllMovies();
    }

    @QueryMapping
    public Movie movieById(@Argument Long id) {
        return movieService.getMovieById(id);
    }

    @MutationMapping
    public Movie addMovie(@Argument String title, @Argument String description, @Argument String genreName) {
        return movieService.addMovie(title, description, genreName);
    }

    @MutationMapping
    public Movie updateMovie(@Argument Long id, @Argument String title, @Argument String description, @Argument String genreName) {
        return movieService.updateMovie(id, title, description, genreName);
    }

    @MutationMapping
    public Movie deleteMovie(@Argument Long id) {
        return movieService.deleteMovie(id);
    }

}
