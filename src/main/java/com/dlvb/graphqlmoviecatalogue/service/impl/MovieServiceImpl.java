package com.dlvb.graphqlmoviecatalogue.service.impl;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.repository.MovieRepository;
import com.dlvb.graphqlmoviecatalogue.service.GenreService;
import com.dlvb.graphqlmoviecatalogue.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * An implementation of a {@link MovieService}.
 * @author Matushkin Anton
 */
@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    @NonNull
    private final MovieRepository movieRepository;

    @NonNull
    private final GenreService genreService;

    @Override
    public Movie addMovie(String title, String description, String genreName) {
        Genre genre = genreService.findGenreByName(genreName);
        if (genre == null) {
            genre = genreService.addGenre(genreName);
        }
        return movieRepository.save(Movie.builder()
                .title(title)
                .description(description)
                .genre(genre)
                .build());
    }

    @Override
    public Movie updateMovie(Long id, String title, String description, String genreName) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        if (title != null) {
            movie.setTitle(title);
        }

        if (genreName != null) {
            Genre genre = genreService.findGenreByName(genreName);
            movie.setGenre(genre);
        }
        if (description != null) {
            movie.setDescription(description);
        }

        return movieRepository.save(movie);
    }

    @Override
    public Movie deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        movieRepository.delete(movie);
        return movie;
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found"));
    }

    @Override
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

}
