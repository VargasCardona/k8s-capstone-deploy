package microservice.movies.dtos;

public record UpdateMovieDTO(
        Long id,
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
