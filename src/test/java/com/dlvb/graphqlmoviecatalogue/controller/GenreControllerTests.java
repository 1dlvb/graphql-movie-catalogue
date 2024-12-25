package com.dlvb.graphqlmoviecatalogue.controller;

import com.dlvb.graphqlmoviecatalogue.config.GraphQlTestConfig;
import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Testcontainers
@Import(GraphQlTestConfig.class)
class GenreControllerTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private GenreService genreService;

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @BeforeEach
    void setup() {
        Genre drama = new Genre(1L, "Drama");
        Genre comedy = new Genre(2L, "Comedy");
        Genre thriller = new Genre(3L, "Thriller");

        when(genreService.getAllGenres()).thenReturn(Arrays.asList(drama, comedy, thriller));
    }

    @Test
    void testAddGenreSavesGenre() {
        Genre mockGenre = new Genre(4L, "Action");

        when(genreService.addGenre(any())).thenReturn(mockGenre);

        String mutation = """
                mutation {
                  addGenre(name: "Action") {
                    id
                    name
                  }
                }
                """;

        graphQlTester.document(mutation)
                .execute()
                .path("addGenre.id").entity(Long.class).isEqualTo(4L)
                .path("addGenre.name").entity(String.class).isEqualTo("Action");
    }

    @Test
    void testUpdateGenreUpdatesGenre() {
        Genre updatedGenre = new Genre(1L, "Thriller");

        when(genreService.updateGenre(any(), any())).thenReturn(updatedGenre);

        String mutation = """
            mutation {
              updateGenre(id: 1, name: "Thriller") {
                id
                name
              }
            }
            """;

        graphQlTester.document(mutation)
                .execute()
                .path("updateGenre.id").entity(Long.class).isEqualTo(1L)
                .path("updateGenre.name").entity(String.class).isEqualTo("Thriller");
    }

    @Test
    void testDeleteGenreDeletesGenre() {
        Genre deletedGenre = new Genre(2L, "Comedy");

        when(genreService.deleteGenre(any())).thenReturn(deletedGenre);

        String mutation = """
                mutation {
                  deleteGenre(id: 2) {
                    id
                    name
                  }
                }
                """;

        graphQlTester.document(mutation)
                .execute()
                .path("deleteGenre.id").entity(Long.class).isEqualTo(2L)
                .path("deleteGenre.name").entity(String.class).isEqualTo("Comedy");
    }

}
