package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.Search;
import com.dlvb.graphqlmoviecatalogue.grpc.MovieSearchClient;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import com.dlvb.graphqlmoviecatalogue.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller for managing movies.
 * @author Matushkin Anton
 */
@Controller
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Operations for adding, updating, deleting, and searching movies.")
public class MovieController {

    @NonNull
    private final MovieService movieService;

    @NonNull
    private final MovieSearchClient movieSearchClient;

    @NonNull
    private final GenreService genreService;

    /**
     * Retrieves a list of all movies.
     *
     * @return List of all movies
     */
    @QueryMapping
    @Operation(summary = "Get all movies", description = "Gets a list of all movies in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of movies retrieved successfully")
    })
    public List<Movie> movies() {
        return movieService.getAllMovies();
    }

    /**
     * Retrieves a movie by its ID.
     *
     * @param id Movie ID
     * @return Movie with the given ID
     */
    @QueryMapping
    @Operation(summary = "Get movie by ID", description = "Gets a movie by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie found successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found with the given ID")
    })
    public Movie movieById(@Argument Long id) {
        return movieService.getMovieById(id);
    }

    /**
     * Adds a new movie.
     *
     * @param title Movie title
     * @param description Movie description
     * @param genreName Movie genre name
     * @return The added movie
     */
    @MutationMapping
    @Operation(summary = "Add a movie", description = "Adds a new movie to the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Movie addMovie(@Argument String title, @Argument String description, @Argument String genreName) {
        return movieService.addMovie(title, description, genreName);
    }

    /**
     * Updates an existing movie.
     *
     * @param id Movie ID to update
     * @param title New movie title
     * @param description New movie description
     * @param genreName New movie genre
     * @return The updated movie
     */
    @MutationMapping
    @Operation(summary = "Update a movie", description = "Updates a movie's information in the system.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Movie not found with the given ID")
    })
    public Movie updateMovie(@Argument Long id, @Argument String title,
                             @Argument String description, @Argument String genreName) {
        return movieService.updateMovie(id, title, description, genreName);
    }

    /**
     * Deletes a movie by its ID.
     *
     * @param id Movie ID to delete
     * @return The deleted movie
     */
    @MutationMapping
    @Operation(summary = "Delete a movie", description = "Deletes a movie by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movie deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Movie not found with the given ID")
    })
    public Movie deleteMovie(@Argument Long id) {
        return movieService.deleteMovie(id);
    }

    /**
     * Searches for movies based on a query.
     *
     * @param query Search query string
     * @param pageNumber Page number for pagination (default is 0)
     * @param pageSize Page size for pagination (default is 10)
     * @return List of movies matching the search query
     */
    @QueryMapping
    @Operation(summary = "Search for movies", description = "Searches for movies based on a query string with pagination support.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
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
