package com.dlvb.graphqlmoviecatalogue.service.impl;

import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.repository.ReviewRepository;
import com.dlvb.graphqlmoviecatalogue.service.MovieService;
import com.dlvb.graphqlmoviecatalogue.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * An implementation of a {@link ReviewService}.
 * @author Matushkin Anton
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    @NonNull
    private final ReviewRepository reviewRepository;

    @NonNull
    private final MovieService movieService;

    @Override
    public Review addReview(Long movieId, String comment, int rating) {
        Movie movie = movieService.getMovieById(movieId);
        return reviewRepository.save(Review.builder()
                .comment(comment)
                .rating(rating)
                .movie(movie)
                .build());
    }

    @Override
    public Review updateReview(Long id, String comment, Integer rating) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        if (comment != null) {
            review.setComment(comment);
        }

        if (rating != null) {
            review.setRating(rating);
        }

        return reviewRepository.save(review);
    }

    @Override
    public Review deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        reviewRepository.delete(review);
        return review;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

}

