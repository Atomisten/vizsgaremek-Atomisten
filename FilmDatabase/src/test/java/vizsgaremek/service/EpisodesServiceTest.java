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
import vizsgaremek.domain.archive.DeletedEpisodes;
import vizsgaremek.dto.DeletedEpisodesInfo;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.repository.DeletedSeriesAndEpisodesRepository;
import vizsgaremek.repository.EpisodesRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@AutoConfigureMockMvc
public class EpisodesServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    EpisodesRepository episodesRepository;

    @Mock
    DeletedSeriesAndEpisodesRepository deletedSeriesAndEpisodesRepository;

    @Mock
    SeriesService seriesService;

    private final ModelMapper modelMapper = new ModelMapper();

    private EpisodesService episodesService;
    private Episodes episodes;
    private Episodes episodes2;
    private EpisodesInfo episodesInfo;
    private EpisodesInfo episodesInfo2;
    private EpisodeCommand episodeCommand;
    private EpisodeCommand episodeCommand2;
    private DeletedEpisodes deletedEpisodes;
    private DeletedEpisodesInfo deletedEpisodesInfo;


    @BeforeEach
    void init() {

        episodeCommand = new EpisodeCommand("episodeTitle", "episodeDirector");
        episodeCommand2 = new EpisodeCommand("episodeTitle2", "episodeDirector2");
        episodesInfo = new EpisodesInfo(1, "episodeTitle", "episodeDirector", "seriesTitle");
        episodesInfo2 = new EpisodesInfo(1, "episodeTitle2", "episodeDirector2", "seriesTitle");
        episodes = new Episodes(1, "episodeTitle", "episodeDirector", null);
        episodes2 = new Episodes(2, "episodeTitle2", "episodeDirector2", null);
        System.out.println("...");
        Series series = new Series(1, "seriesTitle", "seriesAuthor",
                new ArrayList<>(List.of(episodes, episodes2)));
        episodes.setSeries(series);
        episodes2.setSeries(series);

        episodesService = new EpisodesService(episodesRepository, modelMapper, seriesService, deletedSeriesAndEpisodesRepository);
    }

    @Test
    void testSaveEpisodes_oneEpisodes_success() throws Exception {
        when(episodesRepository.save(any())).thenReturn(episodes);
        assertThat(episodesService.saveEpisode(1, episodeCommand))
                .extracting(EpisodesInfo::getTitle)
                .isEqualTo(episodesInfo.getTitle());
    }

    @Test
    void testSaveEpisodes_oneEpisodes_fail() {
        when(episodesRepository.save(any())).thenReturn(episodes);
        assertThat(episodesService.saveEpisode(1, episodeCommand))
                .extracting(EpisodesInfo::getTitle)
                .isNotEqualTo(episodesInfo.getDirector());
    }

    @Test
    void testFindEpisodesById_success() {
        when(episodesRepository.findById(1)).thenReturn(episodes);
        assertThat(episodesService.findById(1))
                .extracting(EpisodesInfo::getTitle)
                .isEqualTo(episodes.getTitle());
    }

    @Test
    void testFindEpisodesById_fail() {
        assertThrows(IllegalArgumentException.class, () -> episodesService.findById(2));
    }

    @Test
    void listAllEpisodes_twoEpisodes_success() {
        when(episodesRepository.listAll()).thenReturn(List.of(episodes, episodes2));
        assertThat(episodesService.listAllEpisodes().size()).isEqualTo(2);
    }

}