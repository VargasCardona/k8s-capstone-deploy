package microservice.videogames.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.videogames.dtos.SaveVideogameDTO;
import microservice.videogames.dtos.UpdateVideogameDTO;
import microservice.videogames.model.Videogame;
import microservice.videogames.repository.VideogameRepository;
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
public class VideogameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VideogameRepository videogameRepository;

    private Videogame testVideogame;
    private SaveVideogameDTO saveVideogameDTO;
    private UpdateVideogameDTO updateVideogameDTO;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        videogameRepository.deleteAll();

        // Crear una película de prueba
        testVideogame = new Videogame();
        testVideogame.setTitle("Interstellar");
        testVideogame.setReleaseYear(2014);
        testVideogame.setDirector("Christopher Nolan");
        testVideogame.setGenre("Sci-Fi");
        testVideogame.setDurationMinutes(169);
        testVideogame.setRating(8.6);

        testVideogame = videogameRepository.save(testVideogame);

        saveVideogameDTO = new SaveVideogameDTO(
                "The Prestige",
                2006,
                "Christopher Nolan",
                "Drama",
                130,
                8.5
        );

        updateVideogameDTO = new UpdateVideogameDTO(
                testVideogame.getId(),
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
    }

    @Test
    void getAllVideogames_ShouldReturnAllVideogames() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/videogames")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getVideogameById_WhenVideogameExists_ShouldReturnVideogame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/videogames/{id}", testVideogame.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getVideogameById_WhenVideogameNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/videogames/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createVideogame_ShouldReturnCreatedVideogame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/videogames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveVideogameDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("The Prestige")))
                .andDo(print());

        List<Videogame> videogames = videogameRepository.findAll();
        assertEquals(2, videogames.size());
    }

    @Test
    void updateVideogame_WhenVideogameExists_ShouldReturnUpdatedVideogame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/videogames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVideogameDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar Updated")))
                .andExpect(jsonPath("$.rating", is(8.7)))
                .andDo(print());
    }

    @Test
    void updateVideogame_WhenVideogameNotExists_ShouldReturnNotFound() throws Exception {
        updateVideogameDTO = new UpdateVideogameDTO(
                99999L,
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/videogames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateVideogameDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteVideogame_ShouldDeleteVideogameAndReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/videogames/{id}", testVideogame.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertFalse(videogameRepository.findById(testVideogame.getId()).isPresent());
    }
}