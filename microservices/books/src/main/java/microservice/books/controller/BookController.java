package microservice.books.controller;

import lombok.RequiredArgsConstructor;
import microservice.books.dtos.SaveBookDTO;
import microservice.books.dtos.UpdateBookDTO;
import microservice.books.interfaces.IBookService;
import microservice.books.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final IBookService _bookService;

    @GetMapping
    public ResponseEntity<List<Book>> listBooks() {
        return ResponseEntity.ok(_bookService.listBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        return _bookService.getBookById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> saveBook(@RequestBody SaveBookDTO dto) {
        Book body = _bookService.saveBook(dto);
        return ResponseEntity.created(URI.create("books/" + body.getId())).body(body);
    }

    @PutMapping
    public ResponseEntity<Book> updateBook(@RequestBody UpdateBookDTO dto) {
        return _bookService.updateBook(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        _bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
