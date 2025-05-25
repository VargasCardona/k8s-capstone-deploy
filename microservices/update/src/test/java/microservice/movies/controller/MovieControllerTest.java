package microservice.movies.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;

public class MovieControllerTest {

    @Mock
    private IMovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private Movie movie;
    private UpdateMovieDTO updateMovieDTO;

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

        updateMovieDTO = new UpdateMovieDTO(
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
    void updateMovie_WhenMovieExists_ShouldReturnUpdatedMovie() {
        // Arrange
        when(movieService.updateMovie(any(UpdateMovieDTO.class))).thenReturn(Optional.of(movie));

        // Act
        ResponseEntity<Movie> response = movieController.updateMovie(updateMovieDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(movieService, times(1)).updateMovie(any(UpdateMovieDTO.class));
    }

    @Test
    void updateMovie_WhenMovieNotExists_ShouldReturnNotFound() {
        // Arrange
        when(movieService.updateMovie(any(UpdateMovieDTO.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Movie> response = movieController.updateMovie(updateMovieDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(movieService, times(1)).updateMovie(any(UpdateMovieDTO.class));
    }

}

