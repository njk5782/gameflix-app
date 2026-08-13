package com.example.gameflix.service;

import com.example.gameflix.model.Movie;
import com.example.gameflix.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MovieService.class)
class MovieServiceTest {

    @Autowired
    private MovieService movieService;

    @MockBean
    private MovieRepository movieRepository;

    @Test
    void getAllMovies_ShouldReturnList() {
        Movie movie = new Movie("The Matrix", "Science Fiction", 1999);
        when(movieRepository.findAll()).thenReturn(List.of(movie));

        List<Movie> movies = movieService.getAllMovies();

        assertFalse(movies.isEmpty());
        assertEquals("The Matrix", movies.get(0).getTitle());
    }

    @Test
    void getMovieById_ShouldReturnMatchingMovie() {
        Movie movie = new Movie("Inception", "Science Fiction", 2010);
        movie.setId(1L);
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));

        Optional<Movie> result = movieService.getMovieById(1L);

        assertEquals("Inception", result.orElseThrow().getTitle());
        verify(movieRepository).findById(1L);
    }

    @Test
    void addMovie_WhenTitleAlreadyExists_ShouldRejectMovie() {
        Movie movie = new Movie("The Matrix", "Science Fiction", 1999);
        when(movieRepository.existsByTitleIgnoreCase("The Matrix")).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieService.addMovie(movie)
        );

        assertEquals("Game title already exists", exception.getMessage());
    }

    @Test
    void addMovie_WhenValid_ShouldSaveMovie() {
        Movie movie = new Movie("Interstellar", "Science Fiction", 2014);
        when(movieRepository.existsByTitleIgnoreCase("Interstellar")).thenReturn(false);
        when(movieRepository.save(movie)).thenReturn(movie);
        Movie savedMovie = movieService.addMovie(movie);
        assertEquals("Interstellar", savedMovie.getTitle());
        verify(movieRepository).save(movie);
    }

    @Test
    void deleteMovie_WhenGameExists_ShouldDeleteGame() {
        when(movieRepository.existsById(1L)).thenReturn(true);

        movieService.deleteMovie(1L);

        verify(movieRepository).deleteById(1L);
    }

    @Test
    void deleteMovie_WhenGameDoesNotExist_ShouldRejectRequest() {
        when(movieRepository.existsById(99L)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> movieService.deleteMovie(99L)
        );

        assertEquals("Game not found", exception.getMessage());
    }
}
