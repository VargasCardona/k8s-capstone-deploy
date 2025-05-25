package microservice.movies.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import microservice.movies.dtos.UpdateMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final IMovieService _movieService;

    @PutMapping
    public ResponseEntity<Movie> updateMovie(@RequestBody UpdateMovieDTO dto) {
        return _movieService.updateMovie(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}
