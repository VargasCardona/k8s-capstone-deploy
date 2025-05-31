package microservice.movies.controller;

import static org.hamcrest.Matchers.hasSize;
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
}