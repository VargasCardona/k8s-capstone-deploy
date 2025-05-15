package microservice.books.service;

import microservice.books.dtos.SaveBookDTO;
import microservice.books.dtos.UpdateBookDTO;
import microservice.books.model.Book;
import microservice.books.repository.BookRepository;
import microservice.books.service.BookService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private SaveBookDTO saveBookDTO;
    private UpdateBookDTO updateBookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book(
                1L,
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );

        saveBookDTO = new SaveBookDTO(
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );

        updateBookDTO = new UpdateBookDTO(
                1L,
                "Inception",
                2010,
                "Christopher Nolan",
                "Sci-Fi",
                148,
                8.8
        );
    }

    @Test
    void listBooks_ShouldReturnAllBooks() {
        // Arrange
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(books);

        // Act
        List<Book> result = bookService.listBooks();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Inception", result.get(0).getTitle());
        verify(bookRepository, times(1)).findAll();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void getBookById_WhenBookNotExists_ShouldReturnEmpty() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.getBookById(1L);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void saveBook_ShouldReturnSavedBook() {
        // Arrange
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Book result = bookService.saveBook(saveBookDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Inception", result.getTitle());
        assertEquals(2010, result.getReleaseYear());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        // Act
        Optional<Book> result = bookService.updateBook(updateBookDTO);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Inception", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void updateBook_WhenBookNotExists_ShouldReturnEmpty() {
        // Arrange
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act
        Optional<Book> result = bookService.updateBook(updateBookDTO);

        // Assert
        assertFalse(result.isPresent());
        verify(bookRepository, times(1)).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_ShouldCallDeleteById() {
        // Arrange
        doNothing().when(bookRepository).deleteById(anyLong());

        // Act
        bookService.deleteBook(1L);

        // Assert
        verify(bookRepository, times(1)).deleteById(1L);
    }
}

