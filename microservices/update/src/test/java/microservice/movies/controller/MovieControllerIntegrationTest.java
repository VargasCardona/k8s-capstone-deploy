package microservice.movies.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.model.Movie;
import microservice.movies.repository.MovieRepository;

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

}