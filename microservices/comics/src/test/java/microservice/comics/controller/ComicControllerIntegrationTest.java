package microservice.comics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.comics.dtos.SaveComicDTO;
import microservice.comics.dtos.UpdateComicDTO;
import microservice.comics.model.Comic;
import microservice.comics.repository.ComicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
public class ComicControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ComicRepository comicRepository;

    private Comic testComic;
    private SaveComicDTO saveComicDTO;
    private UpdateComicDTO updateComicDTO;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        comicRepository.deleteAll();

        // Crear una película de prueba
        testComic = new Comic();
        testComic.setTitle("Interstellar");
        testComic.setReleaseYear(2014);
        testComic.setDirector("Christopher Nolan");
        testComic.setGenre("Sci-Fi");
        testComic.setDurationMinutes(169);
        testComic.setRating(8.6);

        testComic = comicRepository.save(testComic);

        saveComicDTO = new SaveComicDTO(
                "The Prestige",
                2006,
                "Christopher Nolan",
                "Drama",
                130,
                8.5
        );

        updateComicDTO = new UpdateComicDTO(
                testComic.getId(),
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
    }

    @Test
    void getAllComics_ShouldReturnAllComics() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comics")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getComicById_WhenComicExists_ShouldReturnComic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comics/{id}", testComic.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getComicById_WhenComicNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comics/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createComic_ShouldReturnCreatedComic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/comics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveComicDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("The Prestige")))
                .andDo(print());

        List<Comic> comics = comicRepository.findAll();
        assertEquals(2, comics.size());
    }

    @Test
    void updateComic_WhenComicExists_ShouldReturnUpdatedComic() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/comics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComicDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar Updated")))
                .andExpect(jsonPath("$.rating", is(8.7)))
                .andDo(print());
    }

    @Test
    void updateComic_WhenComicNotExists_ShouldReturnNotFound() throws Exception {
        updateComicDTO = new UpdateComicDTO(
                99999L,
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/comics")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateComicDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteComic_ShouldDeleteComicAndReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/comics/{id}", testComic.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertFalse(comicRepository.findById(testComic.getId()).isPresent());
    }
}