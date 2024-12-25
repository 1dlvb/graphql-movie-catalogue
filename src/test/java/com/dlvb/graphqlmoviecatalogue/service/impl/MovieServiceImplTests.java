package com.dlvb.graphqlmoviecatalogue.service.impl;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.repository.MovieRepository;
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

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Import(MovieServiceImpl.class)
@ExtendWith(SpringExtension.class)
class MovieServiceImplTests {

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
    private MovieRepository movieRepository;

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private MovieServiceImpl movieService;

    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Test
    void testAddMovieCreatesMovie() {
        Genre genre = genreService.addGenre("Action1");
        Movie movie = movieService.addMovie("Inception",
                "thriller", "Action1");

        assertNotNull(movie.getId());
        assertEquals("Inception", movie.getTitle());
        assertEquals("Action1", movie.getGenre().getName());
    }

    @Test
    void testUpdateMovieUpdatesMovie() {
        Movie movie = movieService.addMovie("Inception",
                "thriller", "Action");
        Movie updatedMovie = movieService.updateMovie(movie.getId(), "Interstellar",
                "epic", "Adventure");

        assertEquals("Interstellar", updatedMovie.getTitle());
        assertEquals("epic", updatedMovie.getDescription());
        assertEquals("Adventure", updatedMovie.getGenre().getName());
    }

    @Test
    void testUpdateMovieWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                movieService.updateMovie(999L, "Interstellar", "epic", "Adventure"));

        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    void testDeleteMovieDeletesMovie() {
        Movie movie = movieService.addMovie("Inception", "thriller", "Action");
        Movie deletedMovie = movieService.deleteMovie(movie.getId());

        assertEquals("Inception", deletedMovie.getTitle());
        assertFalse(movieRepository.findById(movie.getId()).isPresent());
    }

    @Test
    void testDeleteMovieWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                movieService.deleteMovie(999L));

        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    void testGetMovieByIdReturnsMovieById() {
        Movie movie = movieService.addMovie("Inception", "thriller", "Action");
        Movie foundMovie = movieService.getMovieById(movie.getId());

        assertEquals("Inception", foundMovie.getTitle());
        assertEquals("Action", foundMovie.getGenre().getName());
    }

    @Test
    void testGetMovieByIdWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                movieService.getMovieById(999L));

        assertEquals("Movie not found", exception.getMessage());
    }

    @Test
    void testGetAllMoviesReturnsListOfMovies() {
        movieService.addMovie("Inception", "thriller", "Action");
        movieService.addMovie("Interstellar", "epic", "Adventure");

        List<Movie> movies = movieService.getAllMovies();

        assertEquals(2, movies.size());
    }
}
