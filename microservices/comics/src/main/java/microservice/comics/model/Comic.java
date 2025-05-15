package microservice.comics.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "comics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Comic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Integer releaseYear;
    private String director;
    private String genre;
    private Integer durationMinutes;
    private Double rating;
}
