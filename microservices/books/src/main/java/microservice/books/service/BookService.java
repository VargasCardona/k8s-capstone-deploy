package microservice.books.service;

import lombok.RequiredArgsConstructor;
import microservice.books.dtos.SaveBookDTO;
import microservice.books.dtos.UpdateBookDTO;
import microservice.books.interfaces.IBookService;
import microservice.books.model.Book;
import microservice.books.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final BookRepository bookRepository;

    public List<Book> listBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book saveBook(SaveBookDTO dto) {
        Book book = new Book(
                null,
                dto.title(),
                dto.releaseYear(),
                dto.director(),
                dto.genre(),
                dto.durationMinutes(),
                dto.rating()
        );
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(UpdateBookDTO dto) {
        return bookRepository.findById(dto.id()).map(existingBook -> {
            existingBook.setTitle(dto.title());
            existingBook.setReleaseYear(dto.releaseYear());
            existingBook.setDirector(dto.director());
            existingBook.setGenre(dto.genre());
            existingBook.setDurationMinutes(dto.durationMinutes());
            existingBook.setRating(dto.rating());
            return bookRepository.save(existingBook);
        });
    }

    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }

}
