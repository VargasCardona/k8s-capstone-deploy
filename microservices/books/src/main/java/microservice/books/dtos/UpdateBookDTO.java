package microservice.books.dtos;

public record UpdateBookDTO(
        Long id,
        String title,
        Integer releaseYear,
        String director,
        String genre,
        Integer durationMinutes,
        Double rating
) {}
