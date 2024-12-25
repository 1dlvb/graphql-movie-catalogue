package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.config.GraphQlTestConfig;
import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@Import(GraphQlTestConfig.class)
@ExtendWith(SpringExtension.class)
class ReviewControllerTests {

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
    private GraphQlTester graphQlTester;

    @MockBean
    private ReviewService reviewService;

    private Movie movie;
    private Review review;

    @BeforeEach
    void setUp() {
        Genre genre = new Genre(1L, "Thriller");
        movie = new Movie(1L, "Inception", "thriller", genre, null);
        review = new Review(1L, "Great movie", 5, movie);
    }

    @Test
    void testAddReview() {
        when(reviewService.addReview(1L, "Great movie", 5)).thenReturn(review);

        String mutation = "mutation { " +
                "addReview(movieId: 1, comment: \"Great movie\", rating: 5) { " +
                "id, comment, rating, movie { id, title, genre { id, name } } " +
                "} " +
                "}";

        graphQlTester.document(mutation)
                .execute()
                .path("addReview")
                .entity(Review.class)
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getComment()).isEqualTo("Great movie");
                    assertThat(r.getRating()).isEqualTo(5);
                    assertThat(r.getMovie()).isNotNull();
                    assertThat(r.getMovie().getTitle()).isEqualTo("Inception");
                    assertThat(r.getMovie().getGenre().getName()).isEqualTo("Thriller");
                });
    }

    @Test
    void testUpdateReview() {
        Review updatedReview = new Review(1L, "Amazing movie", 5, movie);
        when(reviewService.updateReview(1L, "Amazing movie", 5)).thenReturn(updatedReview);

        String mutation = "mutation { " +
                "updateReview(id: 1, comment: \"Amazing movie\", rating: 5) { " +
                "id, comment, rating, movie { id, title, genre { id, name } } " +
                "} " +
                "}";

        graphQlTester.document(mutation)
                .execute()
                .path("updateReview")
                .entity(Review.class)
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getComment()).isEqualTo("Amazing movie");
                    assertThat(r.getRating()).isEqualTo(5);
                    assertThat(r.getMovie().getTitle()).isEqualTo("Inception");
                    assertThat(r.getMovie().getGenre().getName()).isEqualTo("Thriller");
                });
    }

    @Test
    void testDeleteReview() {
        when(reviewService.deleteReview(1L)).thenReturn(review);

        String mutation = "mutation { " +
                "deleteReview(id: 1) { " +
                "id, comment, rating, movie { id, title, genre { id, name } } " +
                "} " +
                "}";

        graphQlTester.document(mutation)
                .execute()
                .path("deleteReview")
                .entity(Review.class)
                .satisfies(r -> {
                    assertThat(r.getId()).isEqualTo(1L);
                    assertThat(r.getComment()).isEqualTo("Great movie");
                    assertThat(r.getRating()).isEqualTo(5);
                    assertThat(r.getMovie().getTitle()).isEqualTo("Inception");
                    assertThat(r.getMovie().getGenre().getName()).isEqualTo("Thriller");
                });
    }

}
