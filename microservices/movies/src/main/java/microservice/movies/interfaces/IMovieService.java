package microservice.movies.interfaces;

import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.model.Movie;

import java.util.List;
import java.util.Optional;

public interface IMovieService {

    List<Movie> listMovies();

    Optional<Movie> getMovieById(Long id);

    Movie saveMovie(SaveMovieDTO dto);

    Optional<Movie> updateMovie(UpdateMovieDTO dto);

    void deleteMovie(Long id);

}
