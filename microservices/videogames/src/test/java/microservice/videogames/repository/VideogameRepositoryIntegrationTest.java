package microservice.videogames.repository;

import microservice.videogames.model.Videogame;
import microservice.videogames.repository.VideogameRepository;

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
public class VideogameRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VideogameRepository videogameRepository;

    @Test
    void whenFindById_thenReturnVideogame() {
        // Arrange
        Videogame videogame = new Videogame();
        videogame.setTitle("The Matrix");
        videogame.setReleaseYear(1999);
        videogame.setDirector("Wachowski Sisters");
        videogame.setGenre("Sci-Fi");
        videogame.setDurationMinutes(136);
        videogame.setRating(8.7);

        entityManager.persist(videogame);
        entityManager.flush();

        // Act
        Optional<Videogame> found = videogameRepository.findById(videogame.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("The Matrix", found.get().getTitle());
    }

    @Test
    void whenFindAll_thenReturnAllVideogames() {
        // Arrange
        Videogame videogame1 = new Videogame();
        videogame1.setTitle("The Matrix");
        videogame1.setReleaseYear(1999);
        videogame1.setDirector("Wachowski Sisters");
        videogame1.setGenre("Sci-Fi");
        videogame1.setDurationMinutes(136);
        videogame1.setRating(8.7);

        Videogame videogame2 = new Videogame();
        videogame2.setTitle("Inception");
        videogame2.setReleaseYear(2010);
        videogame2.setDirector("Christopher Nolan");
        videogame2.setGenre("Sci-Fi");
        videogame2.setDurationMinutes(148);
        videogame2.setRating(8.8);

        entityManager.persist(videogame1);
        entityManager.persist(videogame2);
        entityManager.flush();

        // Act
        List<Videogame> videogames = videogameRepository.findAll();

        // Assert
        assertEquals(2, videogames.size());
    }

    @Test
    void whenSave_thenReturnSavedVideogame() {
        // Arrange
        Videogame videogame = new Videogame();
        videogame.setTitle("The Dark Knight");
        videogame.setReleaseYear(2008);
        videogame.setDirector("Christopher Nolan");
        videogame.setGenre("Action");
        videogame.setDurationMinutes(152);
        videogame.setRating(9.0);

        // Act
        Videogame saved = videogameRepository.save(videogame);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("The Dark Knight", saved.getTitle());
    }

    @Test
    void whenDelete_thenVideogameShouldBeRemoved() {
        // Arrange
        Videogame videogame = new Videogame();
        videogame.setTitle("The Dark Knight");
        videogame.setReleaseYear(2008);
        videogame.setDirector("Christopher Nolan");
        videogame.setGenre("Action");
        videogame.setDurationMinutes(152);
        videogame.setRating(9.0);

        entityManager.persist(videogame);
        entityManager.flush();

        Long id = videogame.getId();

        // Act
        videogameRepository.deleteById(id);
        Optional<Videogame> found = videogameRepository.findById(id);

        // Assert
        assertFalse(found.isPresent());
    }
}
