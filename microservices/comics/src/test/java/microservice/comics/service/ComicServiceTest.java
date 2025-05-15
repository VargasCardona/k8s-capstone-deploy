package microservice.comics.service;

import microservice.comics.dtos.SaveComicDTO;
import microservice.comics.dtos.UpdateComicDTO;
import microservice.comics.model.Comic;
import microservice.comics.repository.ComicRepository;
import microservice.comics.service.ComicService;

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

public class ComicServiceTest {

    @Mock
    private ComicRepository comicRepository;

    @InjectMocks
    private ComicService comicService;

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
    void listComics_ShouldReturnAllComics() {
        // Arrange
        List<Comic> comics = Collections.singletonList(comic);
        when(comicRepository.findAll()).thenReturn(comics);

        // Act
        List<Comic> result = comicService.listComics();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(comicRepository, times(1)).findAll();
    }

    @Test
    void getComicById_WhenComicExists_ShouldReturnComic() {
        // Arrange
        when(comicRepository.findById(anyLong())).thenReturn(Optional.of(comic));

        // Act
        Optional<Comic> result = comicService.getComicById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(comicRepository, times(1)).findById(1L);
    }

    @Test
    void getComicById_WhenComicNotExists_ShouldReturnEmpty() {
        // Arrange
        when(comicRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Comic> result = comicService.getComicById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(comicRepository, times(1)).findById(1L);
    }

    @Test
    void saveComic_ShouldReturnSavedComic() {
        // Arrange
        when(comicRepository.save(any(Comic.class))).thenReturn(comic);

        // Act
        Comic result = comicService.saveComic(saveComicDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        verify(comicRepository, times(1)).save(any(Comic.class));
    }

    @Test
    void updateComic_WhenComicExists_ShouldReturnUpdatedComic() {
        // Arrange
        when(comicRepository.findById(anyLong())).thenReturn(Optional.of(comic));
        when(comicRepository.save(any(Comic.class))).thenReturn(comic);

        // Act
        Optional<Comic> result = comicService.updateComic(updateComicDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(comicRepository, times(1)).findById(1L);
        verify(comicRepository, times(1)).save(any(Comic.class));
    }

    @Test
    void updateComic_WhenComicNotExists_ShouldReturnEmpty() {
        // Arrange
        when(comicRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Comic> result = comicService.updateComic(updateComicDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(comicRepository, times(1)).findById(1L);
        verify(comicRepository, never()).save(any(Comic.class));
    }

    @Test
    void deleteComic_ShouldCallDeleteById() {
        // Arrange
        doNothing().when(comicRepository).deleteById(anyLong());

        // Act
        comicService.deleteComic(1L);

        // Assert
        verify(comicRepository, times(1)).deleteById(1L);
    }
}

