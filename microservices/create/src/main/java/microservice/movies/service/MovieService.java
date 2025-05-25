package microservice.movies.service;

import lombok.RequiredArgsConstructor;
import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;
import microservice.movies.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService implements IMovieService {

    private final MovieRepository movieRepository;

    public List<Movie> listMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie(SaveMovieDTO dto) {
        Movie movie = new Movie(
                null,
                dto.title(),
                dto.releaseYear(),
                dto.director(),
                dto.genre(),
                dto.durationMinutes(),
                dto.rating()
        );
        return movieRepository.save(movie);
    }

    public Optional<Movie> updateMovie(UpdateMovieDTO dto) {
        return movieRepository.findById(dto.id()).map(existingMovie -> {
            existingMovie.setTitle(dto.title());
            existingMovie.setReleaseYear(dto.releaseYear());
            existingMovie.setDirector(dto.director());
            existingMovie.setGenre(dto.genre());
            existingMovie.setDurationMinutes(dto.durationMinutes());
            existingMovie.setRating(dto.rating());
            return movieRepository.save(existingMovie);
        });
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

}
