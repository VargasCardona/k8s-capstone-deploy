package microservice.books.dtos;

public record SaveBookDTO(
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
