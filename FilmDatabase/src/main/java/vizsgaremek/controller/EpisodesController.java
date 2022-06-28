package vizsgaremek.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.DeletedEpisodesInfo;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.service.EpisodesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/")
public class EpisodesController {

    private EpisodesService episodesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EpisodesController.class);


    public EpisodesController(EpisodesService episodesService) {
        this.episodesService = episodesService;
    }

    @PostMapping("series/{seriesId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public EpisodesInfo save(@PathVariable("seriesId") Integer id, @Valid @RequestBody EpisodeCommand command) {
        LOGGER.info("Http request, POST /api/series/" + id + "/episodes , body: " + command.toString());
        return episodesService.saveEpisode(id, command);
    }

    @GetMapping("series/Allepisodes")
    @ResponseStatus(HttpStatus.OK)
    public List<EpisodesInfo> findAll() {
        LOGGER.info("Http request, GET api/series/Allepisodes");
        return episodesService.listAllEpisodes();
    }

    @GetMapping("series/{seriesId}/episodes/")
    @ResponseStatus(HttpStatus.OK)
    public List<EpisodesInfo> findAllEpisodesForSeries(@PathVariable("seriesId") Integer id) {
        LOGGER.info("Http request, GET /api/series/" + id + "/episodes");
        return episodesService.listAllEpisodesForSeries(id);
    }

    @GetMapping("series/Allepisodes/{episodeId}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodesInfo findById(@PathVariable("episodeId") Integer id) {
        LOGGER.info("Http request, GET /api/series/Allepisodes/" + id);
        return episodesService.findById(id);
    }

    @PutMapping("series/Allepisodes/{episodeId}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodesInfo updateOrInsert(@PathVariable("episodeId") Integer id, @RequestBody EpisodeCommand command) {
        LOGGER.info("Http request, PUT /api/series/Allepisodes/" + id);
        return episodesService.updateOrInsert(id, command);

    }

    //ha nagyon véletlen a felhasználónak kéne. bár nem kéne hogy kelljen neki:

//    @PutMapping("Allepisodes/{episodeId}/setSeries")
//    @ResponseStatus(HttpStatus.OK)
//    public EpisodesInfo setSeries(@PathVariable("episodeId") Integer episodeId,@Valid @RequestBody Integer seriesId) {
//            return episodesService.setSeries(episodeId,seriesId);
//        }

    @DeleteMapping("series/Allepisodes/{episodeId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("episodeId") Integer id) {
        LOGGER.info("Http request, DELETE /api/series/Allepisodes/" + id);
        episodesService.deleteById(id);
    }


    @GetMapping("/archive/Alldeletedepisodes")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedEpisodesInfo> archiveList() {
        LOGGER.info("Http request, GET /api/archive/Alldeletedepisodes");
        return episodesService.archiveList();
    }

    @DeleteMapping("/archive/deletedepisodes/{deletedEpisodeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArchivedEpisode(@PathVariable("deletedEpisodeId") Integer id) {
        LOGGER.info("Http request, DELETE /api/archive/deletedepisodes/" + id);
        episodesService.deleteArchivedEpisodeById(id);
    }

    @DeleteMapping("/archive/deletedepisodes/PURGE")
    @ResponseStatus(HttpStatus.OK)                               //Easter Egg
    public void purge() {
        LOGGER.info("Http request, DELETE /api/archive/deletedepisodes/PURGE... What have you done?!");
        episodesService.purge();
    }

}
