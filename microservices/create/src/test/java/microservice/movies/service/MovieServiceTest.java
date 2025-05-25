package microservice.movies.service;

import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.model.Movie;
import microservice.movies.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

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
    void listMovies_ShouldReturnAllMovies() {
        // Arrange
        List<Movie> movies = Collections.singletonList(movie);
        when(movieRepository.findAll()).thenReturn(movies);

        // Act
        List<Movie> result = movieService.listMovies();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    void getMovieById_WhenMovieExists_ShouldReturnMovie() {
        // Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));

        // Act
        Optional<Movie> result = movieService.getMovieById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void getMovieById_WhenMovieNotExists_ShouldReturnEmpty() {
        // Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Movie> result = movieService.getMovieById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(movieRepository, times(1)).findById(1L);
    }

    @Test
    void saveMovie_ShouldReturnSavedMovie() {
        // Arrange
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Act
        Movie result = movieService.saveMovie(saveMovieDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldReturnUpdatedMovie() {
        // Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Act
        Optional<Movie> result = movieService.updateMovie(updateMovieDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    void updateMovie_WhenMovieNotExists_ShouldReturnEmpty() {
        // Arrange
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Movie> result = movieService.updateMovie(updateMovieDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(movieRepository, times(1)).findById(1L);
        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    void deleteMovie_ShouldCallDeleteById() {
        // Arrange
        doNothing().when(movieRepository).deleteById(anyLong());

        // Act
        movieService.deleteMovie(1L);

        // Assert
        verify(movieRepository, times(1)).deleteById(1L);
    }
}

