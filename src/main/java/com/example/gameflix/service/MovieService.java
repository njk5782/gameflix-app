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
        if (movie == null || movie.getTitle() == null || movie.getTitle().isBlank()) {
            throw new IllegalArgumentException("Game title is required");
        }
        movie.setTitle(movie.getTitle().trim());
        if (movie.getGenre() == null || movie.getGenre().isBlank()) {
            throw new IllegalArgumentException("Game genre is required");
        }
        if (movie.getReleaseYear() == null || movie.getReleaseYear() < 1888 || movie.getReleaseYear() > 2100) {
            throw new IllegalArgumentException("Enter a valid release year");
        }

        if (movieRepository.existsByTitleIgnoreCase(movie.getTitle())) {
            throw new IllegalArgumentException("Game title already exists");
        }

        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new IllegalArgumentException("Game not found");
        }

        movieRepository.deleteById(id);
    }
}
