package com.dlvb.graphqlmoviecatalogue.service;

import com.dlvb.graphqlmoviecatalogue.model.Movie;

import java.util.List;

/**
 * Service interface for managing movies in the movie catalogue.
 */
public interface MovieService {

    /**
     * Adds a new movie to the catalogue.
     *
     * @param title       the title of the movie
     * @param description a brief description of the movie
     * @param genreName   the name of the genre the movie belongs to
     * @return the created Movie object
     */
    Movie addMovie(String title, String description, String genreName);

    /**
     * Updates an existing movie with new details.
     *
     * @param id          the ID of the movie to be updated
     * @param title       the new title of the movie
     * @param description the new description of the movie
     * @param genreName   the new genre name of the movie
     * @return the updated Movie object
     */
    Movie updateMovie(Long id, String title, String description, String genreName);

    /**
     * Deletes a movie from the catalogue.
     *
     * @param id the ID of the movie to be deleted
     * @return the deleted Movie object
     */
    Movie deleteMovie(Long id);

    /**
     * Retrieves a movie by its ID.
     *
     * @param id the ID of the movie to be retrieved
     * @return the Movie object if found, or null if no movie with the given ID exists
     */
    Movie getMovieById(Long id);

    /**
     * Retrieves all movies in the catalogue.
     *
     * @return a list of all movies
     */
    List<Movie> getAllMovies();

}
