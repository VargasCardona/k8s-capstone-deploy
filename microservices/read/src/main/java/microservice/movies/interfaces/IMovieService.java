package microservice.movies.interfaces;

import java.util.List;
import java.util.Optional;

import microservice.movies.model.Movie;

public interface IMovieService {

    List<Movie> listMovies();

    Optional<Movie> getMovieById(Long id);

}
