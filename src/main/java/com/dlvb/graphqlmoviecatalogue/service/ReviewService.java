package com.dlvb.graphqlmoviecatalogue.service;

import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    public Review addReview(Long movieId, String comment, int rating) {
        Movie movie = movieService.getMovieById(movieId);
        return reviewRepository.save(Review.builder()
                .comment(comment)
                .rating(rating)
                .movie(movie)
                .build());
    }

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

    public Review deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        reviewRepository.delete(review);
        return review;
    }

    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

}

