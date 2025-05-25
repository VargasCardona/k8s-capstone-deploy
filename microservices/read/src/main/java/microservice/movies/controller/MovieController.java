package microservice.movies.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final IMovieService _movieService;

    @GetMapping
    public ResponseEntity<List<Movie>> listMovies() {
        return ResponseEntity.ok(_movieService.listMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return _movieService.getMovieById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
