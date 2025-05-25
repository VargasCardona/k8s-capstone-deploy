package microservice.movies.interfaces;

import java.util.Optional;

import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.model.Movie;

public interface IMovieService {

    Optional<Movie> updateMovie(UpdateMovieDTO dto);

}
