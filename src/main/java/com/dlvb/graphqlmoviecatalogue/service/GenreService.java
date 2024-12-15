package com.dlvb.graphqlmoviecatalogue.service;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.repository.GenreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre addGenre(String name) {
        return genreRepository.save(Genre.builder()
                .name(name)
                .build());
    }

    public Genre updateGenre(Long id, String name) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        genre.setName(name);
        return genreRepository.save(genre);
    }

    public Genre deleteGenre(Long id) {
        Genre genre = genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre not found"));
        genreRepository.delete(genre);
        return genre;
    }

    public Genre findGenreByName(String name) {
        return genreRepository.findGenreByName(name);
    }

    public Genre findGenreById(Long id) {
        return genreRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Genre with id %s is not found.", id)));
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

}
