package vizsgaremek.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.service.MoviesService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(controllers = MoviesController.class)
public class MoviesControllerWebMvcIT {

    @MockBean
    MoviesService moviesService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testFindAll_twoMovies_success() throws Exception {
        when(moviesService.listAllMovies())
                .thenReturn(List.of(

                        new MoviesInfo(1, "tesztTitle", "tesztAuthor", 600),
                        new MoviesInfo(2, "2ndtesztTitle", "2ndtesztAuthor", 600)
                ));
        mockMvc.perform(get("/api/movies")).
                andDo(print());
    }
}
