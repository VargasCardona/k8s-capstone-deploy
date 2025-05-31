package microservice.movies.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

import microservice.movies.dtos.SaveMovieDTO;
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
    private SaveMovieDTO saveMovieDTO;

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
}