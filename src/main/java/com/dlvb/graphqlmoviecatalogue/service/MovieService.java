package com.dlvb.graphqlmoviecatalogue.service;

import com.dlvb.graphqlmoviecatalogue.model.Genre;
import com.dlvb.graphqlmoviecatalogue.model.Movie;
import com.dlvb.graphqlmoviecatalogue.repository.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreService genreService;

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

    public Movie deleteMovie(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
        movieRepository.delete(movie);
        return movie;
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Movie not found"));
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

}
