package microservice.books.interfaces;

import microservice.books.dtos.SaveBookDTO;
import microservice.books.dtos.UpdateBookDTO;
import microservice.books.model.Book;

import java.util.List;
import java.util.Optional;

public interface IBookService {

    List<Book> listBooks();

    Optional<Book> getBookById(Long id);

    Book saveBook(SaveBookDTO dto);

    Optional<Book> updateBook(UpdateBookDTO dto);

    void deleteBook(Long id);

}
