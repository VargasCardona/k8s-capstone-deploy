package microservice.comics.service;

import lombok.RequiredArgsConstructor;
import microservice.comics.dtos.SaveComicDTO;
import microservice.comics.dtos.UpdateComicDTO;
import microservice.comics.interfaces.IComicService;
import microservice.comics.model.Comic;
import microservice.comics.repository.ComicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComicService implements IComicService {

    private final ComicRepository comicRepository;

    public List<Comic> listComics() {
        return comicRepository.findAll();
    }

    public Optional<Comic> getComicById(Long id) {
        return comicRepository.findById(id);
    }

    public Comic saveComic(SaveComicDTO dto) {
        Comic comic = new Comic(
                null,
                dto.title(),
                dto.releaseYear(),
                dto.director(),
                dto.genre(),
                dto.durationMinutes(),
                dto.rating()
        );
        return comicRepository.save(comic);
    }

    public Optional<Comic> updateComic(UpdateComicDTO dto) {
        return comicRepository.findById(dto.id()).map(existingComic -> {
            existingComic.setTitle(dto.title());
            existingComic.setReleaseYear(dto.releaseYear());
            existingComic.setDirector(dto.director());
            existingComic.setGenre(dto.genre());
            existingComic.setDurationMinutes(dto.durationMinutes());
            existingComic.setRating(dto.rating());
            return comicRepository.save(existingComic);
        });
    }

    public void deleteComic(Long id) {
        comicRepository.deleteById(id);
    }

}
