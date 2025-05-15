package microservice.comics.dtos;

public record UpdateComicDTO(
        Long id,
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
