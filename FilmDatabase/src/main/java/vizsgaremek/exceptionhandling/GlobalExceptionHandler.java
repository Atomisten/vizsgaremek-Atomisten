package vizsgaremek.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(MovieNotFoundException.class)
//    @ResponseStatus(value =HttpStatus.NOT_FOUND)
//    public void handleMovieNotFound() {
//        log.info("Movie not found with id");;
//    }

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleParticipantNotFound(MovieNotFoundException exception) {
        ValidationError validationError = new ValidationError("movie_id",
                "Movie with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(EpisodeNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleParticipantNotFound(EpisodeNotFoundException exception) {
        ValidationError validationError = new ValidationError("episode_id",
                "Episode with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeriesNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleParticipantNotFound(SeriesNotFoundException exception) {
        ValidationError validationError = new ValidationError("series_id",
                "Series with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeletedEpisodeNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleParticipantNotFound(DeletedEpisodeNotFoundException exception) {
        ValidationError validationError = new ValidationError("deletion_id",
                "DeletedEpisode with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }
}
