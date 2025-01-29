package com.dlvb.graphqlmoviecatalogue.service;

import com.dlvb.graphqlmoviecatalogue.model.Genre;

import java.util.List;

/**
 * Service interface for managing genres in the movie catalogue.
 * @author Matushkin Anton
 */
public interface GenreService {

    /**
     * Adds a new genre to the catalogue.
     *
     * @param name the name of the genre to be added
     * @return the created Genre object
     */
    Genre addGenre(String name);

    /**
     * Updates an existing genre with a new name.
     *
     * @param id   the ID of the genre to be updated
     * @param name the new name of the genre
     * @return the updated Genre object
     */
    Genre updateGenre(Long id, String name);

    /**
     * Deletes a genre from the catalogue.
     *
     * @param id the ID of the genre to be deleted
     * @return the deleted Genre object
     */
    Genre deleteGenre(Long id);

    /**
     * Finds a genre by its name.
     *
     * @param name the name of the genre to be found
     * @return the Genre object if found, or null if no genre with the given name exists
     */
    Genre findGenreByName(String name);

    /**
     * Finds a genre by its ID.
     *
     * @param id the ID of the genre to be found
     * @return the Genre object if found, or null if no genre with the given ID exists
     */
    Genre findGenreById(Long id);

    /**
     * Retrieves all genres in the catalogue.
     *
     * @return a list of all genres
     */
    List<Genre> getAllGenres();

}
