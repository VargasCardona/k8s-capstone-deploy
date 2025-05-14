package microservice.comics.interfaces;

import microservice.comics.dtos.SaveComicDTO;
import microservice.comics.dtos.UpdateComicDTO;
import microservice.comics.model.Comic;

import java.util.List;
import java.util.Optional;

public interface IComicService {

    List<Comic> listComics();

    Optional<Comic> getComicById(Long id);

    Comic saveComic(SaveComicDTO dto);

    Optional<Comic> updateComic(UpdateComicDTO dto);

    void deleteComic(Long id);

}
