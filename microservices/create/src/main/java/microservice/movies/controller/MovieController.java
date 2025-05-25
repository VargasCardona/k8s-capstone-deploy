package microservice.movies.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import microservice.movies.dtos.SaveMovieDTO;
import microservice.movies.interfaces.IMovieService;
import microservice.movies.model.Movie;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final IMovieService _movieService;

    @PostMapping
    public ResponseEntity<Movie> saveMovie(@RequestBody SaveMovieDTO dto) {
        Movie body = _movieService.saveMovie(dto);
        return ResponseEntity.created(URI.create("movies/" + body.getId())).body(body);
    }

}
