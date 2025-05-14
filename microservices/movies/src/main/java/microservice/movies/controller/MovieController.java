package microservice.movies.controller;

import lombok.RequiredArgsConstructor;
import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @PostMapping
    public ResponseEntity<Movie> saveMovie(@RequestBody SaveMovieDTO dto) {
        Movie body = _movieService.saveMovie(dto);
        return ResponseEntity.created(URI.create("movies/" + body.getId())).body(body);
    }

    @PutMapping
    public ResponseEntity<Movie> updateMovie(@RequestBody UpdateMovieDTO dto) {
        return _movieService.updateMovie(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        _movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
