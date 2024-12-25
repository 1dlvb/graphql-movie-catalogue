package com.dlvb.graphqlmoviecatalogue.service.impl;

import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Testcontainers
@Import(ReviewServiceImpl.class)
@ExtendWith(SpringExtension.class)
class ReviewServiceImplTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MovieServiceImpl movieService;

    @Autowired
    private ReviewServiceImpl reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository.deleteAll();
    }

    @Test
    void testAddReviewCreatesReview() {
        Movie movie = movieService.addMovie("Inception", "thriller", "Action");
        Review review = reviewService.addReview(movie.getId(), "Great movie", 5);

        assertNotNull(review.getId());
        assertEquals("Great movie", review.getComment());
        assertEquals(5, review.getRating());
        assertEquals(movie.getId(), review.getMovie().getId());
    }

    @Test
    void testUpdateReviewUpdatesReview() {
        Movie movie = movieService.addMovie("Inception", "thriller", "Action");
        Review review = reviewService.addReview(movie.getId(), "Good movie", 4);

        Review updatedReview = reviewService.updateReview(review.getId(), "Amazing movie", 5);

        assertEquals("Amazing movie", updatedReview.getComment());
        assertEquals(5, updatedReview.getRating());
    }

    @Test
    void testUpdateReviewWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                reviewService.updateReview(999L, "Doesn't matter", 3));

        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    void testDeleteReviewDeletesReview() {
        Movie movie = movieService.addMovie("Inception", "thriller", "Action");
        Review review = reviewService.addReview(movie.getId(), "Good movie", 4);

        Review deletedReview = reviewService.deleteReview(review.getId());

        assertEquals("Good movie", deletedReview.getComment());
        assertFalse(reviewRepository.findById(review.getId()).isPresent());
    }

    @Test
    void testDeleteReviewWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                reviewService.deleteReview(999L));

        assertEquals("Review not found", exception.getMessage());
    }

    @Test
    void testGetAllReviewsReturnsListOfReviews() {
        Movie movie1 = movieService.addMovie("Inception", "thriller", "Action");
        Movie movie2 = movieService.addMovie("Interstellar", "epic", "Adventure");

        reviewService.addReview(movie1.getId(), "Amazing", 5);
        reviewService.addReview(movie2.getId(), "Good, but long.", 4);

        List<Review> reviews = reviewService.getAllReviews();

        assertEquals(2, reviews.size());
    }
}
