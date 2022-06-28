package vizsgaremek.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.domain.archive.DeletedMovies;
import vizsgaremek.dto.DeletedMoviesInfo;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.service.MoviesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/movies")
public class MoviesController {

    private MoviesService moviesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(MoviesController.class);

    @Autowired
    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MoviesInfo save(@Valid @RequestBody MovieCommand command) {
        LOGGER.info("Http request, POST /api/movies, body: " + command.toString());
        return moviesService.saveMovie(command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MoviesInfo> findAll() {
        LOGGER.info("Http request, GET /api/movies");
        return moviesService.listAllMovies();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MoviesInfo findById(@PathVariable("id") Integer id) {
        LOGGER.info("Http request, GET /api/movies/" + id);
        return moviesService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MoviesInfo updateOrInsert(@PathVariable("id") Integer id, @RequestBody MovieCommand command) {
        LOGGER.info("Http request, PUT /api/movies/" + id);
        return moviesService.updateOrInsert(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        LOGGER.info("Http request, DELETE /api/movies/" + id);
        moviesService.deleteById(id);
    }


    @GetMapping("/archive")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedMoviesInfo> archiveList() {
        LOGGER.info("Http request, GET /api/movies/archive");
        return moviesService.archiveList();
    }

}
