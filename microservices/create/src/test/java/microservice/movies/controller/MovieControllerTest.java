package microservice.movies.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;

public class MovieControllerTest {

    @Mock
    private IMovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private Movie movie;
    private SaveMovieDTO saveMovieDTO;

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

        saveMovieDTO = new SaveMovieDTO(
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );
    }

    @Test
    void createMovie_ShouldReturnCreatedMovie() {
        // Arrange
        when(movieService.saveMovie(any(SaveMovieDTO.class))).thenReturn(movie);

        // Act
        ResponseEntity<Movie> response = movieController.saveMovie(saveMovieDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(movieService, times(1)).saveMovie(any(SaveMovieDTO.class));
    }

}

