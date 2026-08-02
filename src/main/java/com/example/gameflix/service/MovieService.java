package com.example.gameflix.service;

import com.example.gameflix.model.Movie;
import com.example.gameflix.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie addMovie(Movie movie) {
        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Movie title is required");
        }

        if (movieRepository.existsByTitleIgnoreCase(movie.getTitle())) {
            throw new IllegalArgumentException("Movie title already exists");
        }

        return movieRepository.save(movie);
    }
}
