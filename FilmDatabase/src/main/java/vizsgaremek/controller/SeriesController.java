package vizsgaremek.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import vizsgaremek.dto.DeletedSeriesInfo;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.SeriesInfoFull;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.service.SeriesService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "SERIES controller")
public class SeriesController {


    private SeriesService seriesService;
    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesController.class);

    public SeriesController(SeriesService seriesService) {
        this.seriesService = seriesService;
    }

    @PostMapping("series")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a Series")
    @ApiResponse(responseCode = "201", description = "Series saved")
    public SeriesInfo save(@Valid @RequestBody SeriesCommand command) {
        LOGGER.info("Http request, POST /api/series, body: " + command.toString());
        return seriesService.saveSeries(command);
    }

    @GetMapping("Allseries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all Series")
    public List<SeriesInfo> findAll() {
        LOGGER.info("Http request, GET /api/Allseries");
        return seriesService.listAllSeries();
    }

    @GetMapping("series/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find a series by id and List all the episodes of it")
    public SeriesInfoFull findSeriesByIdFull(@PathVariable("seriesId") Integer seriesId) {
        LOGGER.info("Http request, GET /api/series/" + seriesId);
        return seriesService.findByIdFull(seriesId);
    }

    @PutMapping("series/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a series by id")
    @ApiResponse(responseCode = "200", description = "Series updated")
    public SeriesInfo updateOrInsert(@PathVariable("seriesId") Integer id, SeriesCommand seriesCommand) {
        LOGGER.info("Http request, PUT /api/series/" + id);
        return seriesService.updateOrInsertSeries(id, seriesCommand);
    }


    @DeleteMapping("series/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete an series by id")
    @ApiResponse(responseCode = "200", description = "Series deleted")
    public void deleteSeries(@PathVariable("seriesId") Integer id) {
        LOGGER.info("Http request, DELETE series/" + id);
        seriesService.deleteById(id);
    }


    @GetMapping("archive/Alldeletedseries")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "List all ARCHIVED series")
    public List<DeletedSeriesInfo> archiveList() {
        LOGGER.info("Http request, GET /archive/Alldeletedseries/");
        return seriesService.archiveList();
    }


    @DeleteMapping("archive/deletedseries/{seriesId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Delete an ARCHIVED series by id and all the ARCHIVED episodes of it")
    @ApiResponse(responseCode = "200", description = "ARCHIVED Series and all of its episodes deleted")
    public void deleteArchivedSeries(@PathVariable("seriesId") Integer id) {
        LOGGER.info("Http request, DELETE archive/deletedseries/" + id);
        seriesService.deleteArchivedSeries(id);
    }

    @DeleteMapping("archive/deletedseries/PURGE")
    @ResponseStatus(HttpStatus.OK)                               //Easter Egg
    @Operation(summary = "DELETE EVERYTHING ARCHIVED (not recommended)")
    @ApiResponse(responseCode = "200", description = "nothing is archived anymore...")
    public void purge() {
        LOGGER.info("Http request, DELETE archive/deletedseries/PURGE... What have you done?!");
        seriesService.purgeAll();
    }


}
