package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.config.GraphQlTestConfig;
import com.dlvb.graphqlmoviecatalogue.grpc.MovieSearchClient;
import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.model.Review;
import com.dlvb.graphqlmoviecatalogue.repository.GenreRepository;
import com.dlvb.graphqlmoviecatalogue.repository.MovieRepository;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import com.dlvb.graphqlmoviecatalogue.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Import(GraphQlTestConfig.class)
@ExtendWith(MockitoExtension.class)
class MovieControllerTests {

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
    private GenreRepository genreRepository;

    @Autowired
    private MovieRepository movieRepository;
    @Mock
    private GenreService genreService;

    @Mock
    private MovieService movieService;

    @Mock
    private MovieSearchClient movieSearchClient;
    @InjectMocks
    private MovieController movieController;

    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void testMoviesQueryReturnsListOfMovies() {
        Genre genre = genreRepository.save(new Genre(1L, "Action"));
        movieRepository.save(new Movie(1L, "Expected Title", "Description", genre, null));

        String query = """
            query {
                movies {
                    id
                    title
                    description
                    genre {
                        id
                        name
                    }
                }
            }
        """;

        graphQlTester.document(query)
                .execute()
                .path("data.movies[0].title")
                .entity(String.class)
                .isEqualTo("Expected Title");
    }

    @Test
    void testMovieByIdQueryReturnsQueryById() {
        Genre genre = genreRepository.save(new Genre(1L, "Action"));
        Movie movie = movieRepository.save(new Movie(1L, "Expected Title", "Description", genre, null));

        String query = """
            query($id: ID!) {
                movieById(id: $id) {
                    id
                    title
                    description
                    genre {
                        id
                        name
                    }
                }
            }
        """;

        graphQlTester.document(query)
                .variable("id", movie.getId())
                .execute()
                .path("data.movieById.title")
                .entity(String.class)
                .isEqualTo("Expected Title");
    }

    @Test
    void testAddMovieMutationCreatesMovie() {
        genreRepository.save(new Genre(1L, "Action"));

        String mutation = """
            mutation($title: String!, $description: String!, $genreName: String!) {
                addMovie(title: $title, description: $description, genreName: $genreName) {
                    id
                    title
                    description
                    genre {
                        id
                        name
                    }
                }
            }
        """;

        graphQlTester.document(mutation)
                .variable("title", "New Movie")
                .variable("description", "New Description")
                .variable("genreName", "Action")
                .execute()
                .path("data.addMovie.title")
                .entity(String.class)
                .isEqualTo("New Movie");
    }

    @Test
    void testUpdateMovieMutationUpdatesMovie() {
        Genre genre = genreRepository.save(new Genre(1L, "Action"));
        Movie movie = movieRepository.save(new Movie(1L, "Old Title", "Old Description", genre, null));

        String mutation = """
            mutation($id: ID!, $title: String!, $description: String!, $genreName: String!) {
                 updateMovie(id: $id, title: $title, description: $description, genreName: $genreName) {
                     id
                     title
                     description
                     genre {
                         id
                         name
                     }
                 }
             }
                     
        """;

        graphQlTester.document(mutation)
                .variable("id", movie.getId())
                .variable("title", "Updated Title")
                .variable("description", "Updated Description")
                .variable("genreName", "Action")
                .execute()
                .path("data.updateMovie.title")
                .entity(String.class)
                .isEqualTo("Updated Title");
    }

    @Test
    void testDeleteMovieMutationDeletesMovie() {
        Genre genre = genreRepository.save(new Genre(1L, "Action"));
        Movie movie = new Movie(1L, "Movie to Delete", "Description", genre, new ArrayList<>());
        movie.getReviews().add(new Review(1L, "Amazing movie", 1, movie));
        movieRepository.save(movie);

        assertThat(movieRepository.findById(movie.getId())).isPresent();

        String mutation = """
        mutation($id: ID!) {
            deleteMovie(id: $id) {
                id
                title
                description
                genre {
                    id
                    name
                }
            }
        }
    """;

        graphQlTester.document(mutation)
                .variable("id", movie.getId())
                .execute()
                .path("data.deleteMovie.title")
                .entity(String.class)
                .isEqualTo("Movie to Delete");

        assertThat(movieRepository.findById(movie.getId())).isEmpty();
    }

}
