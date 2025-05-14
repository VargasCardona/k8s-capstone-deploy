package microservice.books.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import microservice.books.dtos.SaveBookDTO;
import microservice.books.dtos.UpdateBookDTO;
import microservice.books.model.Book;
import microservice.books.repository.BookRepository;
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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
@Transactional
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookRepository bookRepository;

    private Book testBook;
    private SaveBookDTO saveBookDTO;
    private UpdateBookDTO updateBookDTO;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada prueba
        bookRepository.deleteAll();

        // Crear una película de prueba
        testBook = new Book();
        testBook.setTitle("Interstellar");
        testBook.setReleaseYear(2014);
        testBook.setDirector("Christopher Nolan");
        testBook.setGenre("Sci-Fi");
        testBook.setDurationMinutes(169);
        testBook.setRating(8.6);

        testBook = bookRepository.save(testBook);

        saveBookDTO = new SaveBookDTO(
                "The Prestige",
                2006,
                "Christopher Nolan",
                "Drama",
                130,
                8.5
        );

        updateBookDTO = new UpdateBookDTO(
                testBook.getId(),
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
    }

    @Test
    void getAllBooks_ShouldReturnAllBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getBookById_WhenBookExists_ShouldReturnBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", testBook.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar")))
                .andDo(print());
    }

    @Test
    void getBookById_WhenBookNotExists_ShouldReturnNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void createBook_ShouldReturnCreatedBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveBookDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("The Prestige")))
                .andDo(print());

        List<Book> books = bookRepository.findAll();
        assertEquals(2, books.size());
    }

    @Test
    void updateBook_WhenBookExists_ShouldReturnUpdatedBook() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Interstellar Updated")))
                .andExpect(jsonPath("$.rating", is(8.7)))
                .andDo(print());
    }

    @Test
    void updateBook_WhenBookNotExists_ShouldReturnNotFound() throws Exception {
        updateBookDTO = new UpdateBookDTO(
                99999L,
                "Interstellar Updated",
                2014,
                "Christopher Nolan",
                "Sci-Fi",
                169,
                8.7
        );
        mockMvc.perform(MockMvcRequestBuilders.put("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateBookDTO)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    void deleteBook_ShouldDeleteBookAndReturnNoContent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/{id}", testBook.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());

        assertFalse(bookRepository.findById(testBook.getId()).isPresent());
    }
}