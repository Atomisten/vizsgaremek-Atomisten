package vizsgaremek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.archive.DeletedEpisodes;
import vizsgaremek.dto.archive.dto.DeletedEpisodesInfo;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.service.EpisodesService;
import vizsgaremek.service.SeriesService;

import java.util.List;

@RestController
@RequestMapping("api/")
public class EpisodesController {

    private EpisodesService episodesService;


    public EpisodesController(EpisodesService episodesService, SeriesService seriesService) {
        this.episodesService = episodesService;
    }

    @PostMapping("series/{seriesId}/episodes")
    @ResponseStatus(HttpStatus.CREATED)
    public EpisodesInfo save(@PathVariable("seriesId") Integer id, @RequestBody EpisodeCommand command) {
        return episodesService.saveEpisode(id, command);
    }

    @GetMapping("series/ALLepisodes")
    @ResponseStatus(HttpStatus.OK)
    public List<EpisodesInfo> findAll() {
        return episodesService.listAllEpisodes();
    }

    @GetMapping("series/{seriesId}/episodes/")
    @ResponseStatus(HttpStatus.OK)
    public List<EpisodesInfo> findAllEpisodesForSeries(@PathVariable("seriesId") Integer id) {
        return episodesService.listAllEpisodesForSeries(id);
    }

    @GetMapping("series/ALLepisodes/{episodeId}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodesInfo findById(@PathVariable("episodeId") Integer id) {
        return episodesService.findById(id);
    }

    @PutMapping("series/ALLepisodes/{episodeId}")
    @ResponseStatus(HttpStatus.OK)
    public EpisodesInfo updateOrInsert(@PathVariable("episodeId") Integer id, @RequestBody EpisodeCommand command) {
        return episodesService.updateOrInsert(id, command);

    }
//    @PutMapping("Allepisodes/{episodeId}/setSeries")
//    @ResponseStatus(HttpStatus.OK)
//    public EpisodesInfo setSeries(@PathVariable("episodeId") Integer episodeId, @RequestBody Integer seriesId) {
//            return episodesService.setSeries(episodeId,seriesId);
//        }

    @DeleteMapping("series/ALLepisodes/{episodeId}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable("episodeId") Integer id) {
        episodesService.deleteById(id);
    }


    @GetMapping("/archive/ALLdeletedepisodes")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedEpisodesInfo> archiveList() {
        return episodesService.archiveList();
    }

    @DeleteMapping("/archive/deletedepisodes/{deletedEpisodeId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArchivedEpisode(@PathVariable("deletedEpisodeId") Integer id){
        episodesService.deleteArchivedEpisodeById(id);
    }

    @DeleteMapping("/archive/deletedepisodes/PURGE")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)                               //Easter Egg
    public void purge() {
        episodesService.purge();
    }

}
