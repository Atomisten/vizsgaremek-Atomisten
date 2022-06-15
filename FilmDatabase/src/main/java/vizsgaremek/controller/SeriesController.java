package vizsgaremek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.SeriesInfoFull;
import vizsgaremek.dto.archive.dto.DeletedSeriesInfo;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.service.SeriesService;

import java.util.List;

@RestController
@RequestMapping("api/")
public class SeriesController {


    private SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping("series")
    @ResponseStatus(HttpStatus.CREATED)
    public SeriesInfo save(@RequestBody SeriesCommand command) {
        return seriesService.saveSeries(command);
    }

    @GetMapping("Allseries")
    @ResponseStatus(HttpStatus.OK)
    public List<SeriesInfo> findAll() {
        return seriesService.listAllSeries();
    }

    @GetMapping("series/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    public SeriesInfoFull findSeriesByIdFull(@PathVariable("seriesId") Integer seriesId) {
        return seriesService.findByIdFull(seriesId);
    }

    @PutMapping("series/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    public SeriesInfo updateOrInsert(@PathVariable("seriesId") Integer id, SeriesCommand seriesCommand) {
        return seriesService.updateOrInsertSeries(id, seriesCommand);
    }

    @GetMapping("archive/ALLdeletedseries")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedSeriesInfo> archiveList() {
        return seriesService.archiveList();
    }
    @DeleteMapping("series/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteSeries (@PathVariable("seriesId") Integer id) {
        seriesService.deleteById(id);
    }


    @DeleteMapping("archive/deletedseries/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteArchivedSeries (@PathVariable("seriesId") Integer id) {
        seriesService.deleteArchivedSeries(id);
    }

    @DeleteMapping("/archive/deletedseries/PURGE")
    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
    public void purge() {
        seriesService.purgeAll();
    }


}
