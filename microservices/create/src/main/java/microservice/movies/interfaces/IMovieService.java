package microservice.movies.interfaces;

import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.model.Movie;

public interface IMovieService {

    Movie saveMovie(SaveMovieDTO dto);

}
