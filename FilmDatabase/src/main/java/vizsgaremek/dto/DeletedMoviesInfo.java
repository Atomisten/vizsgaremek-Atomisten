package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedMoviesInfo {

    private Integer id;
    private Integer movieId;
    private String title;
    private String author;
    private int costToRent;
    private LocalDateTime timeOfDeletion;
}
