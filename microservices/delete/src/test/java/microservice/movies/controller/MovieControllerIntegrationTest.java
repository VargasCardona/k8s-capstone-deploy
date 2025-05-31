package microservice.movies.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
    private MovieRepository movieRepository;

    private Movie testMovie;

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