package microservice.books.controller;

import microservice.books.controller.BookController;
import microservice.books.dtos.SaveBookDTO;
import microservice.books.dtos.UpdateBookDTO;
import microservice.books.interfaces.IBookService;
import microservice.books.model.Book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class BookControllerTest {

    @Mock
    private IBookService bookService;

    @InjectMocks
    private BookController bookController;

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
    void getAllBooks_ShouldReturnBooksList() {
        // Arrange
        List<Book> books = Collections.singletonList(book);
        when(bookService.listBooks()).thenReturn(books);

        // Act
        ResponseEntity<List<Book>> response = bookController.listBooks();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
        assertEquals("Inception", response.getBody().get(0).getTitle());
        verify(bookService, times(1)).listBooks();
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() {
        // Arrange
        when(bookService.getBookById(anyLong())).thenReturn(Optional.of(book));

        // Act
        ResponseEntity<Book> response = bookController.getBookById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getBookById_WhenBookNotExists_ShouldReturnNotFound() {
        // Arrange
        when(bookService.getBookById(anyLong())).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.getBookById(1L);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void createBook_ShouldReturnCreatedBook() {
        // Arrange
        when(bookService.saveBook(any(SaveBookDTO.class))).thenReturn(book);

        // Act
        ResponseEntity<Book> response = bookController.saveBook(saveBookDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(bookService, times(1)).saveBook(any(SaveBookDTO.class));
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook() {
        // Arrange
        when(bookService.updateBook(any(UpdateBookDTO.class))).thenReturn(Optional.of(book));

        // Act
        ResponseEntity<Book> response = bookController.updateBook(updateBookDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Inception", Objects.requireNonNull(response.getBody()).getTitle());
        verify(bookService, times(1)).updateBook(any(UpdateBookDTO.class));
    }

    @Test
    void updateBook_WhenBookNotExists_ShouldReturnNotFound() {
        // Arrange
        when(bookService.updateBook(any(UpdateBookDTO.class))).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Book> response = bookController.updateBook(updateBookDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService, times(1)).updateBook(any(UpdateBookDTO.class));
    }

    @Test
    void deleteBook_ShouldReturnNoContent() {
        // Arrange
        doNothing().when(bookService).deleteBook(anyLong());

        // Act
        ResponseEntity<Void> response = bookController.deleteBook(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookService, times(1)).deleteBook(1L);
    }
}

