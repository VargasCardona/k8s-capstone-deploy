package microservice.movies.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import microservice.movies.interfaces.IMovieService;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final IMovieService _movieService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        _movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
