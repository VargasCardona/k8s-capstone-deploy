package microservice.movies.dtos;

public record SaveMovieDTO(
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
