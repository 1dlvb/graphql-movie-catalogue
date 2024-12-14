package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @MutationMapping
    public Review addReview(@Argument Long movieId, @Argument String comment, @Argument int rating) {
        return reviewService.addReview(movieId, comment, rating);
    }

    @MutationMapping
    public Review updateReview(@Argument Long id, @Argument String comment, @Argument Integer rating) {
        return reviewService.updateReview(id, comment, rating);
    }

    @MutationMapping
    public Review deleteReview(@Argument Long id) {
        return reviewService.deleteReview(id);
    }

}
