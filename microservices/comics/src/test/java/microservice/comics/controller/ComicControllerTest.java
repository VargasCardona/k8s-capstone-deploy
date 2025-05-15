package microservice.comics.controller;

import microservice.comics.controller.ComicController;
import microservice.comics.dtos.SaveComicDTO;
import microservice.comics.dtos.UpdateComicDTO;
import microservice.comics.interfaces.IComicService;
import microservice.comics.model.Comic;
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

public class ComicControllerTest {

    @Mock
    private IComicService comicService;

    @InjectMocks
    private ComicController comicController;

    private Comic comic;
    private SaveComicDTO saveComicDTO;
    private UpdateComicDTO updateComicDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        comic = new Comic(
                1L,
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );

        saveComicDTO = new SaveComicDTO(
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );

        updateComicDTO = new UpdateComicDTO(
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
    void getAllComics_ShouldReturnComicsList() {
        // Arrange
        List<Comic> comics = Collections.singletonList(comic);
        when(comicService.listComics()).thenReturn(comics);

        // Act
        ResponseEntity<List<Comic>> response = comicController.listComics();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Inception", response.getBody().get(0).getTitle());
        verify(comicService, times(1)).listComics();
    }

    @Test
    void getComicById_WhenComicExists_ShouldReturnComic() {
        // Arrange
        when(comicService.getComicById(anyLong())).thenReturn(Optional.of(comic));

        // Act
        ResponseEntity<Comic> response = comicController.getComicById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(comicService, times(1)).getComicById(1L);
    }

    @Test
    void getComicById_WhenComicNotExists_ShouldReturnNotFound() {
        // Arrange
        when(comicService.getComicById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Comic> response = comicController.getComicById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(comicService, times(1)).getComicById(1L);
    }

    @Test
    void createComic_ShouldReturnCreatedComic() {
        // Arrange
        when(comicService.saveComic(any(SaveComicDTO.class))).thenReturn(comic);

        // Act
        ResponseEntity<Comic> response = comicController.saveComic(saveComicDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(comicService, times(1)).saveComic(any(SaveComicDTO.class));
    }

    @Test
    void updateComic_WhenComicExists_ShouldReturnUpdatedComic() {
        // Arrange
        when(comicService.updateComic(any(UpdateComicDTO.class))).thenReturn(Optional.of(comic));

        // Act
        ResponseEntity<Comic> response = comicController.updateComic(updateComicDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(comicService, times(1)).updateComic(any(UpdateComicDTO.class));
    }

    @Test
    void updateComic_WhenComicNotExists_ShouldReturnNotFound() {
        // Arrange
        when(comicService.updateComic(any(UpdateComicDTO.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Comic> response = comicController.updateComic(updateComicDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(comicService, times(1)).updateComic(any(UpdateComicDTO.class));
    }

    @Test
    void deleteComic_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(comicService).deleteComic(anyLong());

        // Act
        ResponseEntity<Void> response = comicController.deleteComic(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(comicService, times(1)).deleteComic(1L);
    }
}

