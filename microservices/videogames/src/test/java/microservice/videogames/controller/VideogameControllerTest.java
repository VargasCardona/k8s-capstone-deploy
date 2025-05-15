package microservice.videogames.controller;

import microservice.videogames.controller.VideogameController;
import microservice.videogames.dtos.SaveVideogameDTO;
import microservice.videogames.dtos.UpdateVideogameDTO;
import microservice.videogames.interfaces.IVideogameService;
import microservice.videogames.model.Videogame;
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

public class VideogameControllerTest {

    @Mock
    private IVideogameService videogameService;

    @InjectMocks
    private VideogameController videogameController;

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
    void getAllVideogames_ShouldReturnVideogamesList() {
        // Arrange
        List<Videogame> videogames = Collections.singletonList(videogame);
        when(videogameService.listVideogames()).thenReturn(videogames);

        // Act
        ResponseEntity<List<Videogame>> response = videogameController.listVideogames();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Inception", response.getBody().get(0).getTitle());
        verify(videogameService, times(1)).listVideogames();
    }

    @Test
    void getVideogameById_WhenVideogameExists_ShouldReturnVideogame() {
        // Arrange
        when(videogameService.getVideogameById(anyLong())).thenReturn(Optional.of(videogame));

        // Act
        ResponseEntity<Videogame> response = videogameController.getVideogameById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(videogameService, times(1)).getVideogameById(1L);
    }

    @Test
    void getVideogameById_WhenVideogameNotExists_ShouldReturnNotFound() {
        // Arrange
        when(videogameService.getVideogameById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Videogame> response = videogameController.getVideogameById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(videogameService, times(1)).getVideogameById(1L);
    }

    @Test
    void createVideogame_ShouldReturnCreatedVideogame() {
        // Arrange
        when(videogameService.saveVideogame(any(SaveVideogameDTO.class))).thenReturn(videogame);

        // Act
        ResponseEntity<Videogame> response = videogameController.saveVideogame(saveVideogameDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(videogameService, times(1)).saveVideogame(any(SaveVideogameDTO.class));
    }

    @Test
    void updateVideogame_WhenVideogameExists_ShouldReturnUpdatedVideogame() {
        // Arrange
        when(videogameService.updateVideogame(any(UpdateVideogameDTO.class))).thenReturn(Optional.of(videogame));

        // Act
        ResponseEntity<Videogame> response = videogameController.updateVideogame(updateVideogameDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(videogameService, times(1)).updateVideogame(any(UpdateVideogameDTO.class));
    }

    @Test
    void updateVideogame_WhenVideogameNotExists_ShouldReturnNotFound() {
        // Arrange
        when(videogameService.updateVideogame(any(UpdateVideogameDTO.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Videogame> response = videogameController.updateVideogame(updateVideogameDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(videogameService, times(1)).updateVideogame(any(UpdateVideogameDTO.class));
    }

    @Test
    void deleteVideogame_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(videogameService).deleteVideogame(anyLong());

        // Act
        ResponseEntity<Void> response = videogameController.deleteVideogame(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(videogameService, times(1)).deleteVideogame(1L);
    }
}

