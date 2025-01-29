package com.dlvb.graphqlmoviecatalogue.service.impl;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.repository.GenreRepository;
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
@Import(GenreServiceImpl.class)
@ExtendWith(SpringExtension.class)
class GenreServiceImplTests {

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
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
    }

    @Test
    void testAddGenreCreatesGenre() {
        Genre genre = genreService.addGenre("Action");

        assertNotNull(genre.getId());
        assertEquals("Action", genre.getName());
    }

    @Test
    void testUpdateGenreUpdatesGenre() {
        Genre genre = genreService.addGenre("Action");
        Genre updatedGenre = genreService.updateGenre(genre.getId(), "Adventure");

        assertEquals("Adventure", updatedGenre.getName());
    }

    @Test
    void testUpdateGenreWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                genreService.updateGenre(999L, "Adventure"));

        assertEquals("Genre not found", exception.getMessage());
    }

    @Test
    void testDeleteGenreDeletesGenre() {
        Genre genre = genreService.addGenre("Action");
        Genre deletedGenre = genreService.deleteGenre(genre.getId());

        assertEquals("Action", deletedGenre.getName());
        assertFalse(genreRepository.findById(genre.getId()).isPresent());
    }

    @Test
    void testDeleteGenreWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                genreService.deleteGenre(999L));

        assertEquals("Genre not found", exception.getMessage());
    }

    @Test
    void testFindGenreByNameReturnsGenreByName() {
        genreService.addGenre("Action");
        Genre genre = genreService.findGenreByName("Action");

        assertNotNull(genre);
        assertEquals("Action", genre.getName());
    }

    @Test
    void testFindGenreByIdReturnsGenreById() {
        Genre genre = genreService.addGenre("Action");
        Genre foundGenre = genreService.findGenreById(genre.getId());

        assertEquals("Action", foundGenre.getName());
    }

    @Test
    void testFindGenreByIdWhenNotFound() {
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () ->
                genreService.findGenreById(999L));

        assertEquals("Genre with id 999 is not found.", exception.getMessage());
    }

    @Test
    void testGetAllGenresReturnsListOfGenres() {
        genreService.addGenre("Action");
        genreService.addGenre("Adventure");

        List<Genre> genres = genreService.getAllGenres();

        assertEquals(2, genres.size());
    }

}
