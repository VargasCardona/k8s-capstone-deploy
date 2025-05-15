package microservice.comics.dtos;

public record SaveComicDTO(
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
