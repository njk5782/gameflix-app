package com.example.gameflix.controller;

import com.example.gameflix.model.Movie;
import com.example.gameflix.service.MovieService;
import com.example.gameflix.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    private final MovieService movieService;
    public MovieController(MovieService movieService) { this.movieService = movieService; }

    @GetMapping
    public List<Movie> getAllMovies() { return movieService.getAllMovies(); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Movie addMovie(@RequestBody Movie movie) { return movieService.addMovie(movie); }

    @DeleteMapping("/{id}")
    public AuthResponse deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return new AuthResponse("Game removed successfully");
    }
}
