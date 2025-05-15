package microservice.videogames.dtos;

public record UpdateVideogameDTO(
        Long id,
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
