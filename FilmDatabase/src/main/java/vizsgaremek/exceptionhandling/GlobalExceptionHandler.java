package vizsgaremek.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    //    @ExceptionHandler(MovieNotFoundException.class)
//    @ResponseStatus(value =HttpStatus.NOT_FOUND)
//    public void handleMovieNotFound() {
//        log.info("Movie not found with id");;
//    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationError>> handleValidationException(MethodArgumentNotValidException exception) {
        List<ValidationError> validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new ValidationError(fieldError.getField(), fieldError.getDefaultMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleMovieNotFound(MovieNotFoundException exception) {
        ValidationError validationError = new ValidationError("movie_id",
                "Movie with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EpisodeNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleEpisodeNotFound(EpisodeNotFoundException exception) {
        ValidationError validationError = new ValidationError("episode_id",
                "Episode with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SeriesNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleSeriesNotFound(SeriesNotFoundException exception) {
        ValidationError validationError = new ValidationError("series_id",
                "Series with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeletedEpisodeNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleArchivedEpisodeNotFound(DeletedEpisodeNotFoundException exception) {
        ValidationError validationError = new ValidationError("deletion_id",
                "DeletedEpisode with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DeletedSeriesNotFoundException.class)
    public ResponseEntity<List<ValidationError>> handleArchivedSeriesNotFound(DeletedSeriesNotFoundException exception) {
        ValidationError validationError = new ValidationError("deletion_id",
                "DeletedSeries with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }
}
