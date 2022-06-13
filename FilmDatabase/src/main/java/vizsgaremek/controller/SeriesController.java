package vizsgaremek.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.SeriesInfoFull;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.service.SeriesService;

import java.util.List;

@RestController
@RequestMapping("api/series")
public class SeriesController {


    private SeriesService seriesService;

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SeriesInfo save(@RequestBody SeriesCommand command) {
        return seriesService.saveSeries(command);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<SeriesInfo> findAll() {
        return seriesService.listAllSeries();
    }

    @GetMapping("/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    public SeriesInfoFull findSeriesByIdFull(@PathVariable("seriesId") Integer seriesId) {
        return seriesService.findByIdFull(seriesId);
    }

    @PutMapping("/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    public SeriesInfo updateOrInsert(@PathVariable("seriesId") Integer id, SeriesCommand seriesCommand) {
        return seriesService.updateOrInsertSeries(id, seriesCommand);
    }

}
