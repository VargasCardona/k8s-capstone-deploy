package microservice.movies.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.model.Movie;
import microservice.movies.repository.MovieRepository;
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
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MovieRepository movieRepository;

    private Movie testMovie;
    private SaveMovieDTO saveMovieDTO;
    private UpdateMovieDTO updateMovieDTO;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        movieRepository.deleteAll();

        // Crear una película de prueba
        testMovie = new Movie();
        testMovie.setTitle("Interstellar");
        testMovie.setReleaseYear(2014);
        testMovie.setDirector("Christopher Nolan");
        testMovie.setGenre("Sci-Fi");
        testMovie.setDurationMinutes(169);
        testMovie.setRating(8.6);

        testMovie = movieRepository.save(testMovie);

        saveMovieDTO = new SaveMovieDTO(
                "The Prestige",
                2006,
                "Christopher Nolan",
                "Drama",
                130,
                8.5
        );

        updateMovieDTO = new UpdateMovieDTO(
                testMovie.getId(),
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
    }

    @Test
    void getAllMovies_ShouldReturnAllMovies() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movies")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getMovieById_WhenMovieExists_ShouldReturnMovie() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", testMovie.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getMovieById_WhenMovieNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/movies/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createMovie_ShouldReturnCreatedMovie() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveMovieDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("The Prestige")))
                .andDo(print());

        List<Movie> movies = movieRepository.findAll();
        assertEquals(2, movies.size());
    }

    @Test
    void updateMovie_WhenMovieExists_ShouldReturnUpdatedMovie() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMovieDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar Updated")))
                .andExpect(jsonPath("$.rating", is(8.7)))
                .andDo(print());
    }

    @Test
    void updateMovie_WhenMovieNotExists_ShouldReturnNotFound() throws Exception {
        updateMovieDTO = new UpdateMovieDTO(
                99999L,
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateMovieDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteMovie_ShouldDeleteMovieAndReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/movies/{id}", testMovie.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertFalse(movieRepository.findById(testMovie.getId()).isPresent());
    }
}