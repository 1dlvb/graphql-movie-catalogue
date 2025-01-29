
package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller for managing movie genres.
 * @author Matushkin Anton
 */
@Controller
@RequiredArgsConstructor
@Tag(name = "Genre controller")
public class GenreController {

    @NonNull
    private final GenreService genreService;

    /**
     * Adds a new genre with the provided name.
     *
     * @param name the name of the genre to add.
     * @return the newly added genre.
     */
    @MutationMapping
    @Operation(summary = "Add a new genre", description = "Adds a new movie genre with the specified name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public Genre addGenre(@Argument @Parameter(description = "The name of the genre to add.") String name) {
        return genreService.addGenre(name);
    }

    /**
     * Updates an existing genre with the provided ID and new name.
     *
     * @param id   the ID of the genre to update.
     * @param name the new name for the genre.
     * @return the updated genre.
     */
    @MutationMapping
    @Operation(summary = "Update an existing genre", description = "Updates the name of an existing movie genre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    public Genre updateGenre(@Argument @Parameter(description = "The ID of the genre to update.") Long id,
                             @Argument @Parameter(description = "The new name of the genre.") String name) {
        return genreService.updateGenre(id, name);
    }

    /**
     * Deletes a genre by its ID.
     * <p>
     * This mutation will call the {@link GenreService#deleteGenre(Long)} method to delete the genre.
     * </p>
     *
     * @param id the ID of the genre to delete.
     * @return the deleted genre.
     */
    @MutationMapping
    @Operation(summary = "Delete a genre", description = "Deletes an existing movie genre by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Genre deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Genre not found")
    })
    public Genre deleteGenre(@Argument @Parameter(description = "The ID of the genre to delete.") Long id) {
        return genreService.deleteGenre(id);
    }

}
