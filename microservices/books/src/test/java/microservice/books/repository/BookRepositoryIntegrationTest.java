package microservice.books.repository;

import microservice.books.model.Book;
import microservice.books.repository.BookRepository;

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
public class BookRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void whenFindById_thenReturnBook() {
        // Arrange
        Book book = new Book();
        book.setTitle("The Matrix");
        book.setReleaseYear(1999);
        book.setDirector("Wachowski Sisters");
        book.setGenre("Sci-Fi");
        book.setDurationMinutes(136);
        book.setRating(8.7);

        entityManager.persist(book);
        entityManager.flush();

        // Act
        Optional<Book> found = bookRepository.findById(book.getId());

        // Assert
        assertTrue(found.isPresent());
        assertEquals("The Matrix", found.get().getTitle());
    }

    @Test
    void whenFindAll_thenReturnAllBooks() {
        // Arrange
        Book book1 = new Book();
        book1.setTitle("The Matrix");
        book1.setReleaseYear(1999);
        book1.setDirector("Wachowski Sisters");
        book1.setGenre("Sci-Fi");
        book1.setDurationMinutes(136);
        book1.setRating(8.7);

        Book book2 = new Book();
        book2.setTitle("Inception");
        book2.setReleaseYear(2010);
        book2.setDirector("Christopher Nolan");
        book2.setGenre("Sci-Fi");
        book2.setDurationMinutes(148);
        book2.setRating(8.8);

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.flush();

        // Act
        List<Book> books = bookRepository.findAll();

        // Assert
        assertEquals(2, books.size());
    }

    @Test
    void whenSave_thenReturnSavedBook() {
        // Arrange
        Book book = new Book();
        book.setTitle("The Dark Knight");
        book.setReleaseYear(2008);
        book.setDirector("Christopher Nolan");
        book.setGenre("Action");
        book.setDurationMinutes(152);
        book.setRating(9.0);

        // Act
        Book saved = bookRepository.save(book);

        // Assert
        assertNotNull(saved.getId());
        assertEquals("The Dark Knight", saved.getTitle());
    }

    @Test
    void whenDelete_thenBookShouldBeRemoved() {
        // Arrange
        Book book = new Book();
        book.setTitle("The Dark Knight");
        book.setReleaseYear(2008);
        book.setDirector("Christopher Nolan");
        book.setGenre("Action");
        book.setDurationMinutes(152);
        book.setRating(9.0);

        entityManager.persist(book);
        entityManager.flush();

        Long id = book.getId();

        // Act
        bookRepository.deleteById(id);
        Optional<Book> found = bookRepository.findById(id);

        // Assert
        assertFalse(found.isPresent());
    }
}
