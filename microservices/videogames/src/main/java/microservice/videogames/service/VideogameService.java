package microservice.videogames.service;

import lombok.RequiredArgsConstructor;
import microservice.videogames.dtos.SaveVideogameDTO;
import microservice.videogames.dtos.UpdateVideogameDTO;
import microservice.videogames.interfaces.IVideogameService;
import microservice.videogames.model.Videogame;
import microservice.videogames.repository.VideogameRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VideogameService implements IVideogameService {

    private final VideogameRepository videogameRepository;

    public List<Videogame> listVideogames() {
        return videogameRepository.findAll();
    }

    public Optional<Videogame> getVideogameById(Long id) {
        return videogameRepository.findById(id);
    }

    public Videogame saveVideogame(SaveVideogameDTO dto) {
        Videogame videogame = new Videogame(
                null,
                dto.title(),
                dto.releaseYear(),
                dto.director(),
                dto.genre(),
                dto.durationMinutes(),
                dto.rating()
        );
        return videogameRepository.save(videogame);
    }

    public Optional<Videogame> updateVideogame(UpdateVideogameDTO dto) {
        return videogameRepository.findById(dto.id()).map(existingVideogame -> {
            existingVideogame.setTitle(dto.title());
            existingVideogame.setReleaseYear(dto.releaseYear());
            existingVideogame.setDirector(dto.director());
            existingVideogame.setGenre(dto.genre());
            existingVideogame.setDurationMinutes(dto.durationMinutes());
            existingVideogame.setRating(dto.rating());
            return videogameRepository.save(existingVideogame);
        });
    }

    public void deleteVideogame(Long id) {
        videogameRepository.deleteById(id);
    }

}
