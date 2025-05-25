package microservice.movies.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;

public class MovieControllerTest {

    @Mock
    private IMovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private Movie movie;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        movie = new Movie(
                1L,
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );
    }

    @Test
    void getAllMovies_ShouldReturnMoviesList() {
        // Arrange
        List<Movie> movies = Collections.singletonList(movie);
        when(movieService.listMovies()).thenReturn(movies);

        // Act
        ResponseEntity<List<Movie>> response = movieController.listMovies();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Inception", response.getBody().get(0).getTitle());
        verify(movieService, times(1)).listMovies();
    }

    @Test
    void getMovieById_WhenMovieExists_ShouldReturnMovie() {
        // Arrange
        when(movieService.getMovieById(anyLong())).thenReturn(Optional.of(movie));

        // Act
        ResponseEntity<Movie> response = movieController.getMovieById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(movieService, times(1)).getMovieById(1L);
    }

    @Test
    void getMovieById_WhenMovieNotExists_ShouldReturnNotFound() {
        // Arrange
        when(movieService.getMovieById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Movie> response = movieController.getMovieById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(movieService, times(1)).getMovieById(1L);
    }
}

