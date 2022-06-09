package vizsgaremek.exceptionhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
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
        ValidationError validationError = new ValidationError("participantId",
                "Participant with id " + exception.getIdNotFound() + " is not found.");
        return new ResponseEntity<>(List.of(validationError), HttpStatus.NOT_FOUND);
    }
}
