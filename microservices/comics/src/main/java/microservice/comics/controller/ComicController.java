package microservice.comics.controller;

import lombok.RequiredArgsConstructor;
import microservice.comics.dtos.SaveComicDTO;
import microservice.comics.dtos.UpdateComicDTO;
import microservice.comics.interfaces.IComicService;
import microservice.comics.model.Comic;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/comics")
@RequiredArgsConstructor
public class ComicController {

    private final IComicService _comicService;

    @GetMapping
    public ResponseEntity<List<Comic>> listComics() {
        return ResponseEntity.ok(_comicService.listComics());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comic> getComicById(@PathVariable Long id) {
        return _comicService.getComicById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Comic> saveComic(@RequestBody SaveComicDTO dto) {
        Comic body = _comicService.saveComic(dto);
        return ResponseEntity.created(URI.create("comics/" + body.getId())).body(body);
    }

    @PutMapping
    public ResponseEntity<Comic> updateComic(@RequestBody UpdateComicDTO dto) {
        return _comicService.updateComic(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComic(@PathVariable Long id) {
        _comicService.deleteComic(id);
        return ResponseEntity.noContent().build();
    }
}
