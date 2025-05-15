package microservice.comics.repository;

import microservice.comics.model.Comic;
import microservice.comics.repository.ComicRepository;

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
public class ComicRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ComicRepository comicRepository;

    @Test
    void whenFindById_thenReturnComic() {
        // Arrange
        Comic comic = new Comic();
        comic.setTitle("The Matrix");
        comic.setReleaseYear(1999);
        comic.setDirector("Wachowski Sisters");
        comic.setGenre("Sci-Fi");
        comic.setDurationMinutes(136);
        comic.setRating(8.7);

        entityManager.persist(comic);
        entityManager.flush();

        // Act
        Optional<Comic> found = comicRepository.findById(comic.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("The Matrix", found.get().getTitle());
    }

    @Test
    void whenFindAll_thenReturnAllComics() {
        // Arrange
        Comic comic1 = new Comic();
        comic1.setTitle("The Matrix");
        comic1.setReleaseYear(1999);
        comic1.setDirector("Wachowski Sisters");
        comic1.setGenre("Sci-Fi");
        comic1.setDurationMinutes(136);
        comic1.setRating(8.7);

        Comic comic2 = new Comic();
        comic2.setTitle("Inception");
        comic2.setReleaseYear(2010);
        comic2.setDirector("Christopher Nolan");
        comic2.setGenre("Sci-Fi");
        comic2.setDurationMinutes(148);
        comic2.setRating(8.8);

        entityManager.persist(comic1);
        entityManager.persist(comic2);
        entityManager.flush();

        // Act
        List<Comic> comics = comicRepository.findAll();

        // Assert
        assertEquals(2, comics.size());
    }

    @Test
    void whenSave_thenReturnSavedComic() {
        // Arrange
        Comic comic = new Comic();
        comic.setTitle("The Dark Knight");
        comic.setReleaseYear(2008);
        comic.setDirector("Christopher Nolan");
        comic.setGenre("Action");
        comic.setDurationMinutes(152);
        comic.setRating(9.0);

        // Act
        Comic saved = comicRepository.save(comic);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("The Dark Knight", saved.getTitle());
    }

    @Test
    void whenDelete_thenComicShouldBeRemoved() {
        // Arrange
        Comic comic = new Comic();
        comic.setTitle("The Dark Knight");
        comic.setReleaseYear(2008);
        comic.setDirector("Christopher Nolan");
        comic.setGenre("Action");
        comic.setDurationMinutes(152);
        comic.setRating(9.0);

        entityManager.persist(comic);
        entityManager.flush();

        Long id = comic.getId();

        // Act
        comicRepository.deleteById(id);
        Optional<Comic> found = comicRepository.findById(id);

        // Assert
        assertFalse(found.isPresent());
    }
}
