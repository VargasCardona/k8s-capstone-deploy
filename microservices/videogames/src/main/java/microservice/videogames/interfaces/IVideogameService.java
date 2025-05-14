package microservice.videogames.interfaces;

import microservice.videogames.dtos.SaveVideogameDTO;
import microservice.videogames.dtos.UpdateVideogameDTO;
import microservice.videogames.model.Videogame;

import java.util.List;
import java.util.Optional;

public interface IVideogameService {

    List<Videogame> listVideogames();

    Optional<Videogame> getVideogameById(Long id);

    Videogame saveVideogame(SaveVideogameDTO dto);

    Optional<Videogame> updateVideogame(UpdateVideogameDTO dto);

    void deleteVideogame(Long id);

}
