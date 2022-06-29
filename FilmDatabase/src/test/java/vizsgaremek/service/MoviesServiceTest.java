package vizsgaremek.service;

import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.domain.Movies;
import vizsgaremek.domain.archive.DeletedMovies;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.commands.MovieCommand;
import vizsgaremek.repository.MoviesRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, SoftAssertionsExtension.class})
@AutoConfigureMockMvc
public class MoviesServiceTest {

    @Autowired
    MockMvc mockMvc;

    @Mock
    MoviesRepository moviesRepository;

    private final ModelMapper modelMapper = new ModelMapper();

    private MoviesService moviesService;
    private Movies movies;
    private Movies movies2;
    private MoviesInfo moviesInfo;
    private MoviesInfo moviesInfo2;
    private MovieCommand movieCommand;
    private MovieCommand movieCommand2;
    private DeletedMovies deletedMovies;

    @BeforeEach
    void init() {
        movieCommand = new MovieCommand("movieTitle", "movieAuthor");
        movieCommand2 = new MovieCommand("movieTitle2", "movieAuthor2");
        moviesInfo = new MoviesInfo(1, "movieTitle", "movieAuthor", 700);
        moviesInfo2 = new MoviesInfo(1, "movieTitle2", "movieAuthor2", 700);
        movies = new Movies(1, "movieTitle", "movieAuthor", 700);
        movies2 = new Movies(2, "movieTitle2", "movieAuthor2", 700);
        System.out.println("...");

        moviesService = new MoviesService(moviesRepository, modelMapper);
    }

    @Test
    void testSaveMovie_oneMovie_success() throws Exception {
        when(moviesRepository.save(any())).thenReturn(movies);
        assertThat(moviesService.saveMovie(movieCommand))
                .extracting(MoviesInfo::getTitle)
                .isEqualTo(moviesInfo.getTitle());
    }

    @Test
    void testSaveMovie_oneMovie_fail() {
        when(moviesRepository.save(any())).thenReturn(movies);
        assertThat(moviesService.saveMovie(movieCommand))
                .extracting(MoviesInfo::getTitle)
                .isNotEqualTo(moviesInfo.getAuthor());
    }

    @Test
    void testFindMovieById_success() {
        when(moviesRepository.findById(1)).thenReturn(movies);
        assertThat(moviesService.findById(1))
                .extracting(MoviesInfo::getTitle)
                .isEqualTo(movies.getTitle());
    }

    @Test
    void testFindMovieById_fail() {
        assertThrows(IllegalArgumentException.class,  () -> moviesService.findById(2));
    }

    @Test
    void listAllMovies_twoMovies_success() {
        when(moviesRepository.listAll()).thenReturn(List.of(movies, movies2));
        assertThat(moviesService.listAllMovies().size()).isEqualTo(2);
    }

//    @Test
//    void updateOrInsert_oneMovie_success() {
//        when(moviesRepository.updateOrInsert(movies)).thenReturn(movies2);
//        MoviesInfo moviesInfo = moviesService.updateOrInsert(1, movieCommand2);
//        assertThat(moviesInfo.getTitle()).isEqualTo(moviesInfo2.getTitle());
//    }

}
