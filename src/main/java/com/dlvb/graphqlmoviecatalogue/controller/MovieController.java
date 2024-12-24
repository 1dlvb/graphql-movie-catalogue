package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.Search;
import com.dlvb.graphqlmoviecatalogue.grpc.MovieSearchClient;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import com.dlvb.graphqlmoviecatalogue.service.MovieService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MovieController {

    @NonNull
    private final MovieService movieService;
    @NonNull
    private final MovieSearchClient movieSearchClient;
    @NonNull
    private final GenreService genreService;

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
    public Movie updateMovie(@Argument Long id, @Argument String title,
                             @Argument String description, @Argument String genreName) {
        return movieService.updateMovie(id, title, description, genreName);
    }

    @MutationMapping
    public Movie deleteMovie(@Argument Long id) {
        return movieService.deleteMovie(id);
    }

    @QueryMapping
    public List<Movie> searchMovies(
            @Argument String query,
            @Argument(name = "page_number") Integer pageNumber,
            @Argument(name = "page_size") Integer pageSize
    ) {
        pageNumber = (pageNumber == null) ? 0 : pageNumber;
        pageSize = (pageSize == null) ? 10 : pageSize;

        Search.SearchResponse response = movieSearchClient.searchMovies(query, pageNumber, pageSize);

        return response.getMoviesList().stream()
                .map(movie -> Movie.builder()
                        .id(Long.valueOf(movie.getId()))
                        .description(movie.getDescription())
                        .title(movie.getTitle())
                        .genre(genreService.findGenreById(Long.valueOf(movie.getGenreId())))
                        .build())
                .collect(Collectors.toList());
    }

}
