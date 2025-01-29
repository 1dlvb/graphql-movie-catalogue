package com.dlvb.graphqlmoviecatalogue.service.impl;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.repository.GenreRepository;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * An implementation of a {@link GenreService}.
 * @author Matushkin Anton
 */
@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    @NonNull
    private final GenreRepository genreRepository;

    @Override
    public Genre addGenre(String name) {
        return genreRepository.save(Genre.builder()
                .name(name)
                .build());
    }

    @Override
    public Genre updateGenre(Long id, String name) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        genre.setName(name);
        return genreRepository.save(genre);
    }

    @Override
    public Genre deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        genreRepository.delete(genre);
        return genre;
    }

    @Override
    public Genre findGenreByName(String name) {
        return genreRepository.findGenreByName(name);
    }

    @Override
    public Genre findGenreById(Long id) {
        return genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Genre with id %s is not found.", id)));
    }

    @Override
    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

}
