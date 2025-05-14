package microservice.movies.repository;

import microservice.movies.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class MovieRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void whenFindById_thenReturnMovie() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("The Matrix");
        movie.setReleaseYear(1999);
        movie.setDirector("Wachowski Sisters");
        movie.setGenre("Sci-Fi");
        movie.setDurationMinutes(136);
        movie.setRating(8.7);

        entityManager.persist(movie);
        entityManager.flush();

        // Act
        Optional<Movie> found = movieRepository.findById(movie.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("The Matrix", found.get().getTitle());
    }

    @Test
    void whenFindAll_thenReturnAllMovies() {
        // Arrange
        Movie movie1 = new Movie();
        movie1.setTitle("The Matrix");
        movie1.setReleaseYear(1999);
        movie1.setDirector("Wachowski Sisters");
        movie1.setGenre("Sci-Fi");
        movie1.setDurationMinutes(136);
        movie1.setRating(8.7);

        Movie movie2 = new Movie();
        movie2.setTitle("Inception");
        movie2.setReleaseYear(2010);
        movie2.setDirector("Christopher Nolan");
        movie2.setGenre("Sci-Fi");
        movie2.setDurationMinutes(148);
        movie2.setRating(8.8);

        entityManager.persist(movie1);
        entityManager.persist(movie2);
        entityManager.flush();

        // Act
        List<Movie> movies = movieRepository.findAll();

        // Assert
        assertEquals(2, movies.size());
    }

    @Test
    void whenSave_thenReturnSavedMovie() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("The Dark Knight");
        movie.setReleaseYear(2008);
        movie.setDirector("Christopher Nolan");
        movie.setGenre("Action");
        movie.setDurationMinutes(152);
        movie.setRating(9.0);

        // Act
        Movie saved = movieRepository.save(movie);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("The Dark Knight", saved.getTitle());
    }

    @Test
    void whenDelete_thenMovieShouldBeRemoved() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("The Dark Knight");
        movie.setReleaseYear(2008);
        movie.setDirector("Christopher Nolan");
        movie.setGenre("Action");
        movie.setDurationMinutes(152);
        movie.setRating(9.0);

        entityManager.persist(movie);
        entityManager.flush();

        Long id = movie.getId();

        // Act
        movieRepository.deleteById(id);
        Optional<Movie> found = movieRepository.findById(id);

        // Assert
        assertFalse(found.isPresent());
    }
}
