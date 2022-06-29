package vizsgaremek.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.DeletedMoviesInfo;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.service.MoviesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@Tag(name = "MOVIES controller")
public class MoviesController {

    private MoviesService moviesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesController.class);

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a Movie")
    @ApiResponse(responseCode = "201", description = "Movie saved")
    public MoviesInfo save(@Valid @RequestBody MovieCommand command) {
        LOGGER.info("Http request, POST /api/movies, body: " + command.toString());
        return moviesService.saveMovie(command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all movies")
    public List<MoviesInfo> findAll() {
        LOGGER.info("Http request, GET /api/movies");
        return moviesService.listAllMovies();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a movie by id")
    public MoviesInfo findById(@PathVariable("id") Integer id) {
        LOGGER.info("Http request, GET /api/movies/" + id);
        return moviesService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a movie by id")
    @ApiResponse(responseCode = "200", description = "Movie updated")
    public MoviesInfo updateOrInsert(@PathVariable("id") Integer id, @RequestBody MovieCommand command) {
        LOGGER.info("Http request, PUT /api/movies/" + id);
        return moviesService.updateOrInsert(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete a movie by id")
    @ApiResponse(responseCode = "200", description = "Movie deleted")
    public void delete(@PathVariable("id") Integer id) {
        LOGGER.info("Http request, DELETE /api/movies/" + id);
        moviesService.deleteById(id);
    }


    @GetMapping("/archive")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all ARCHIVED movies")
    public List<DeletedMoviesInfo> archiveList() {
        LOGGER.info("Http request, GET /api/movies/archive");
        return moviesService.archiveList();
    }

}
