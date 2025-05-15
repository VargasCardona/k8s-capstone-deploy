package microservice.videogames.service;

import microservice.videogames.dtos.SaveVideogameDTO;
import microservice.videogames.dtos.UpdateVideogameDTO;
import microservice.videogames.model.Videogame;
import microservice.videogames.repository.VideogameRepository;
import microservice.videogames.service.VideogameService;

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

public class VideogameServiceTest {

    @Mock
    private VideogameRepository videogameRepository;

    @InjectMocks
    private VideogameService videogameService;

    private Videogame videogame;
    private SaveVideogameDTO saveVideogameDTO;
    private UpdateVideogameDTO updateVideogameDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        videogame = new Videogame(
                1L,
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );

        saveVideogameDTO = new SaveVideogameDTO(
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );

        updateVideogameDTO = new UpdateVideogameDTO(
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
    void listVideogames_ShouldReturnAllVideogames() {
        // Arrange
        List<Videogame> videogames = Collections.singletonList(videogame);
        when(videogameRepository.findAll()).thenReturn(videogames);

        // Act
        List<Videogame> result = videogameService.listVideogames();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(videogameRepository, times(1)).findAll();
    }

    @Test
    void getVideogameById_WhenVideogameExists_ShouldReturnVideogame() {
        // Arrange
        when(videogameRepository.findById(anyLong())).thenReturn(Optional.of(videogame));

        // Act
        Optional<Videogame> result = videogameService.getVideogameById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(videogameRepository, times(1)).findById(1L);
    }

    @Test
    void getVideogameById_WhenVideogameNotExists_ShouldReturnEmpty() {
        // Arrange
        when(videogameRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Videogame> result = videogameService.getVideogameById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(videogameRepository, times(1)).findById(1L);
    }

    @Test
    void saveVideogame_ShouldReturnSavedVideogame() {
        // Arrange
        when(videogameRepository.save(any(Videogame.class))).thenReturn(videogame);

        // Act
        Videogame result = videogameService.saveVideogame(saveVideogameDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        verify(videogameRepository, times(1)).save(any(Videogame.class));
    }

    @Test
    void updateVideogame_WhenVideogameExists_ShouldReturnUpdatedVideogame() {
        // Arrange
        when(videogameRepository.findById(anyLong())).thenReturn(Optional.of(videogame));
        when(videogameRepository.save(any(Videogame.class))).thenReturn(videogame);

        // Act
        Optional<Videogame> result = videogameService.updateVideogame(updateVideogameDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(videogameRepository, times(1)).findById(1L);
        verify(videogameRepository, times(1)).save(any(Videogame.class));
    }

    @Test
    void updateVideogame_WhenVideogameNotExists_ShouldReturnEmpty() {
        // Arrange
        when(videogameRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Videogame> result = videogameService.updateVideogame(updateVideogameDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(videogameRepository, times(1)).findById(1L);
        verify(videogameRepository, never()).save(any(Videogame.class));
    }

    @Test
    void deleteVideogame_ShouldCallDeleteById() {
        // Arrange
        doNothing().when(videogameRepository).deleteById(anyLong());

        // Act
        videogameService.deleteVideogame(1L);

        // Assert
        verify(videogameRepository, times(1)).deleteById(1L);
    }
}

