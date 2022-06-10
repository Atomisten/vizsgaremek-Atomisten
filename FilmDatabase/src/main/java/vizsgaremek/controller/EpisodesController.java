package vizsgaremek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.archive.DeletedEpisodes;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.service.EpisodesService;

import java.util.List;

@RestController
@RequestMapping("api/episodes")
public class EpisodesController {

    private EpisodesService episodesService;

    public EpisodesController(EpisodesService episodesService) {
        this.episodesService = episodesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EpisodesInfo save(@RequestBody EpisodeCommand command) {
        return episodesService.saveEpisode(command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EpisodesInfo> findAll() {
        return episodesService.listAllEpisodes();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodesInfo findById(@PathVariable("id") Integer id) {
        return episodesService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodesInfo updateOrInsert(@PathVariable("id") Integer id, @RequestBody EpisodeCommand command) {
        return episodesService.updateOrInsert(id, command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("id") Integer id) {
        episodesService.deleteById(id);
    }


    @GetMapping("/archive")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedEpisodes> archiveList() {
        return episodesService.archiveList();
    }

}
