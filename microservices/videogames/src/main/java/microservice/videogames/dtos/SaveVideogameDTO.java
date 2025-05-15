package microservice.videogames.dtos;

public record SaveVideogameDTO(
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
