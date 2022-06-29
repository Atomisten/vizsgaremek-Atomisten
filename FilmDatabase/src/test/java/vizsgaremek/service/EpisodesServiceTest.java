package vizsgaremek.service;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import vizsgaremek.domain.Episodes;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.repository.DeletedSeriesAndMoviesRepository;
import vizsgaremek.repository.EpisodesRepository;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class EpisodesServiceTest {

    @Mock
    EpisodesRepository episodesRepository;

    @Mock
    DeletedSeriesAndMoviesRepository deletedSeriesAndMoviesRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private EpisodesService episodesService;
    private Episodes episodes;
    private EpisodesInfo episodesInfo;

    @BeforeEach
    void init() {
        System.out.println("lol");
    }
}