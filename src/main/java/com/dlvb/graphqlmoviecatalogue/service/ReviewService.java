package com.dlvb.graphqlmoviecatalogue.service;

import com.dlvb.graphqlmoviecatalogue.model.Review;

import java.util.List;

/**
 * Service interface for managing movie reviews in the catalogue.
 */
public interface ReviewService {

    /**
     * Adds a new review for a movie.
     *
     * @param movieId the ID of the movie the review is for
     * @param comment the comment of the review
     * @param rating  the rating of the movie (e.g., from 1 to 5)
     * @return the created Review object
     */
    Review addReview(Long movieId, String comment, int rating);

    /**
     * Updates an existing review with new details.
     *
     * @param id       the ID of the review to be updated
     * @param comment the new comment for the review
     * @param rating  the new rating for the review
     * @return the updated Review object
     */
    Review updateReview(Long id, String comment, Integer rating);

    /**
     * Deletes a review from the catalogue.
     *
     * @param id the ID of the review to be deleted
     * @return the deleted Review object
     */
    Review deleteReview(Long id);

    /**
     * Retrieves all reviews in the catalogue.
     *
     * @return a list of all reviews
     */
    List<Review> getAllReviews();

}
