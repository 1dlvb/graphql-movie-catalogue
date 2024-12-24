package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

/**
 * Controller for managing movie reviews.
 * @author Matushkin Anton
 */
@Controller
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Operations for adding, updating, and deleting movie reviews.")
public class ReviewController {

    @NonNull
    private final ReviewService reviewService;

    /**
     * Adds a new review for a movie.
     *
     * @param movieId The ID of the movie to which the review is related
     * @param comment The review comment
     * @param rating The rating given to the movie
     * @return The added review
     */
    @MutationMapping
    @Operation(summary = "Add a review", description = "Adds a new review for a movie by providing movie ID, comment, and rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review added successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public Review addReview(@Argument Long movieId, @Argument String comment, @Argument int rating) {
        return reviewService.addReview(movieId, comment, rating);
    }

    /**
     * Updates an existing review.
     *
     * @param id The ID of the review to update
     * @param comment The new review comment
     * @param rating The new rating (optional)
     * @return The updated review
     */
    @MutationMapping
    @Operation(summary = "Update a review", description = "Updates an existing review by changing the comment and/or rating.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Review not found with the given ID")
    })
    public Review updateReview(@Argument Long id, @Argument String comment, @Argument Integer rating) {
        return reviewService.updateReview(id, comment, rating);
    }

    /**
     * Deletes an existing review.
     *
     * @param id The ID of the review to delete
     * @return The deleted review
     */
    @MutationMapping
    @Operation(summary = "Delete a review", description = "Deletes an existing review based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found with the given ID")
    })
    public Review deleteReview(@Argument Long id) {
        return reviewService.deleteReview(id);
    }

}
