package vizsgaremek.controller;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.dto.DeletedMoviesInfo;
import vizsgaremek.dto.MoviesInfo;
import vizsgaremek.dto.commands.MovieCommand;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MoviesControllerRestTemplateIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestRestTemplate restTemplate;

    @Value("${cost_to_rent}")
    private int cost_to_rent;

    MovieCommand initMovieCommand1;
    MovieCommand initMovieCommand2;
    MovieCommand initMovieCommand3;

    MoviesInfo initMoviesInfo1;
    MoviesInfo initMoviesInfo2;
    MoviesInfo initMoviesInfo3;


    @BeforeEach
    void init() {

        initMovieCommand1 = new MovieCommand("tesztTitle", "Testopher Nolan");
        initMoviesInfo1 = new MoviesInfo(1, "tesztTitle", "Testopher Nolan", 700);

        initMovieCommand2 = new MovieCommand("tesztTitle2", "Testopher Nolan2");
        initMoviesInfo2 = new MoviesInfo(2, "tesztTitle2", "Testopher Nolan2", 700);

        initMovieCommand3 = new MovieCommand("tesztTitle3", "Testopher Nolan3");
        initMoviesInfo3 = new MoviesInfo(3, "tesztTitle3", "Testopher Nolan3", 700);
    }


    @Test
    @Order(1)
    void testSave_OneMovie_Success() {
        MoviesInfo moviesInfo = restTemplate.postForObject("/api/movies",
                initMovieCommand1, MoviesInfo.class);
        assertThat(moviesInfo)
                .extracting(MoviesInfo::getTitle)
                .isEqualTo(initMoviesInfo1.getTitle());
    }

    @Test
    @Order(2)
    void testFindAll_twoMovies_success() throws Exception {
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[3].title", equalTo("tesztTitle")));

    }

    @Test
    @Order(3)
    void testFindById_OneMovie_Success() throws Exception {
        mockMvc.perform(get("/api/movies/4"))
                .andExpect(jsonPath("$.title", equalTo(initMoviesInfo1.getTitle())))
                .andExpect(jsonPath("$.id", equalTo(4)))
                .andExpect(jsonPath("$.author", equalTo(initMoviesInfo1.getAuthor())))
                .andExpect(jsonPath("$.costToRent", equalTo(cost_to_rent)));
    }

    @Test
    @Order(4)
    void testFindById_OneMovie_Fail() throws Exception {
        mockMvc.perform(get("/api/movies/11"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", is("movie_id")))
                .andExpect(jsonPath("$[0].errorMessage", is("Movie with id 11 is not found.")));
    }

    @Test
    @Order(5)
    void testPutById_OneMovie_success() throws Exception {
        restTemplate.put("/api/movies/4", initMovieCommand2, MoviesInfo.class);
        mockMvc.perform(get("/api/movies/4"))
                .andExpect(jsonPath("$.title", equalTo(initMoviesInfo2.getTitle())));
    }


    @Test
    @Order(6)
    void testDeleteById_OneMovie_success() throws Exception {
        mockMvc.perform(delete("/api/movies/4"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @Order(7)
    void testGetArchiveList_success() throws Exception {
        mockMvc.perform(get("/api/movies/archive"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", equalTo("tesztTitle2")));
    }


}
