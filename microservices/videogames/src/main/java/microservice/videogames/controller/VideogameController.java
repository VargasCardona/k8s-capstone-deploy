package microservice.videogames.controller;

import lombok.RequiredArgsConstructor;
import microservice.videogames.dtos.SaveVideogameDTO;
import microservice.videogames.dtos.UpdateVideogameDTO;
import microservice.videogames.interfaces.IVideogameService;
import microservice.videogames.model.Videogame;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/videogames")
@RequiredArgsConstructor
public class VideogameController {

    private final IVideogameService _videogameService;

    @GetMapping
    public ResponseEntity<List<Videogame>> listVideogames() {
        return ResponseEntity.ok(_videogameService.listVideogames());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Videogame> getVideogameById(@PathVariable Long id) {
        return _videogameService.getVideogameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Videogame> saveVideogame(@RequestBody SaveVideogameDTO dto) {
        Videogame body = _videogameService.saveVideogame(dto);
        return ResponseEntity.created(URI.create("videogames/" + body.getId())).body(body);
    }

    @PutMapping
    public ResponseEntity<Videogame> updateVideogame(@RequestBody UpdateVideogameDTO dto) {
        return _videogameService.updateVideogame(dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVideogame(@PathVariable Long id) {
        _videogameService.deleteVideogame(id);
        return ResponseEntity.noContent().build();
    }
}
