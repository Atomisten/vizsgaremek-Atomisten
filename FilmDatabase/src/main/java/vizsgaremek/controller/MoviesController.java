package vizsgaremek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.MoviesInfoWithoutId;
import vizsgaremek.dto.archive.DeletedMovies;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.service.MoviesService;

import java.util.List;

@RestController
@RequestMapping("api/movies")
public class MoviesController {

    private MoviesService moviesService;

    public MoviesController(MoviesService moviesService) {
        this.moviesService = moviesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MoviesInfo save(@RequestBody MovieCommand command) {
        return moviesService.saveMovie(command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MoviesInfo> findAll() {
        return moviesService.listAllMovies();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MoviesInfo findById(@PathVariable("id") Integer id) {
        return moviesService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MoviesInfo updateOrInsert(@PathVariable("id") Integer id, @RequestBody MovieCommand command) {
        return moviesService.updateOrInsert(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        moviesService.deleteById(id);
    }


    @GetMapping("/archive")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedMovies> archiveList() {
        return moviesService.archiveList();
    }

}
