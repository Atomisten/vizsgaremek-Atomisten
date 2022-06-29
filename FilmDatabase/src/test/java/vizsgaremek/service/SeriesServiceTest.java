package vizsgaremek.service;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import vizsgaremek.domain.Series;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.repository.DeletedSeriesAndMoviesRepository;
import vizsgaremek.repository.SeriesRepository;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class SeriesServiceTest {

    @Mock
    SeriesRepository seriesRepository;

    @Mock
    DeletedSeriesAndMoviesRepository deletedSeriesAndMoviesRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private SeriesService seriesService;
    private Series series;
    private SeriesInfo seriesInfo;

    @BeforeEach
    void init() {
        System.out.println("...");
    }
}