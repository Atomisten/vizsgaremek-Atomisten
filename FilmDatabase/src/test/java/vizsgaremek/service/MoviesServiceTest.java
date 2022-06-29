package vizsgaremek.service;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import vizsgaremek.domain.Movies;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.repository.MoviesRepository;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
public class MoviesServiceTest {

    @Mock
    MoviesRepository moviesRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private MoviesService moviesService;
    private Movies movies;
    private MoviesInfo moviesInfo;

    @BeforeEach
    void init(){
        System.out.println("lol");
    }
}
