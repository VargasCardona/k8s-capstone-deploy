package microservice.movies.controller;

import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MovieControllerTest {

    @Mock
    private IMovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private Movie movie;
    private SaveMovieDTO saveMovieDTO;
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

        saveMovieDTO = new SaveMovieDTO(
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

    @Test
    void deleteMovie_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(movieService).deleteMovie(anyLong());

        // Act
        ResponseEntity<Void> response = movieController.deleteMovie(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(movieService, times(1)).deleteMovie(1L);
    }
}

