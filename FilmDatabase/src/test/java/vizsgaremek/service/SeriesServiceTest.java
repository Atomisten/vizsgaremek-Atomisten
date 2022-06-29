package vizsgaremek.service;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.domain.Episodes;
import vizsgaremek.domain.Series;
import vizsgaremek.domain.archive.DeletedSeries;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.repository.DeletedSeriesAndEpisodesRepository;
import vizsgaremek.repository.SeriesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@AutoConfigureMockMvc
public class SeriesServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    SeriesRepository seriesRepository;

    @Mock
    DeletedSeriesAndEpisodesRepository deletedSeriesAndEpisodesRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private SeriesService seriesService;
    private Series series;
    private Series series2;
    private SeriesInfo seriesInfo;
    private SeriesInfo seriesInfo2;
    private SeriesCommand seriesCommand;
    private SeriesCommand seriesCommand2;
    private DeletedSeries deletedSeries;
    private Episodes episodes;
    private Episodes episodes2;
    private Episodes episodes3;
    private Episodes episodes4;


    @BeforeEach
    void init() {
        episodes = new Episodes(1, "episodeTitle", "episodeDirector", series);
        episodes2 = new Episodes(2, "episodeTitle2", "episodeDirector2", series);
        episodes3 = new Episodes(1, "episodeTitle3", "episodeDirector3", series2);
        episodes4 = new Episodes(2, "episodeTitle4", "episodeDirector4", series2);
        seriesCommand = new SeriesCommand("seriesTitle", "seriesAuthor");
        seriesCommand2 = new SeriesCommand("seriesTitle2", "seriesAuthor2");
        seriesInfo = new SeriesInfo(1, "seriesTitle", "seriesAuthor", 2);
        seriesInfo2 = new SeriesInfo(1, "seriesTitle2", "seriesAuthor2", 2);

        series = new Series(1, "seriesTitle", "seriesAuthor", new ArrayList<>(List.of(episodes, episodes2)));
        series2 = new Series(2, "seriesTitle2", "seriesAuthor2", new ArrayList<>(List.of(episodes3, episodes4)));
        System.out.println("...");

        seriesService = new SeriesService(seriesRepository, deletedSeriesAndEpisodesRepository, modelMapper);
    }

    @Test
    void testSaveSeries_oneSeries_success() throws Exception {
        when(seriesRepository.save(any())).thenReturn(series);
        assertThat(seriesService.saveSeries(seriesCommand))
                .extracting(SeriesInfo::getTitle)
                .isEqualTo(seriesInfo.getTitle());
    }

    @Test
    void testSaveSeries_oneSeries_fail() {
        when(seriesRepository.save(any())).thenReturn(series);
        assertThat(seriesService.saveSeries(seriesCommand))
                .extracting(SeriesInfo::getTitle)
                .isNotEqualTo(seriesInfo.getAuthor());
    }

    @Test
    void testFindSeriesById_success() {
        when(seriesRepository.findById(1)).thenReturn(series);
        assertThat(seriesService.findById(1))
                .extracting(Series::getTitle)
                .isEqualTo(series.getTitle());
    }

//    @Test
//    void testFindSeriesById_fail() {
//        assertThrows(IllegalArgumentException.class, () -> seriesService.findById(5));
//    }

    @Test
    void listAllSeries_twoSeries_success() {
        when(seriesRepository.listAll()).thenReturn(List.of(series, series2));
        assertThat(seriesService.listAllSeries().size()).isEqualTo(2);
    }

}