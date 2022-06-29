package vizsgaremek.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.commands.EpisodeCommand;
import vizsgaremek.dto.commands.SeriesCommand;

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
public class EpisodesControllerRestTemplateIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestRestTemplate restTemplate;


    EpisodeCommand initEpisodeCommand1;
    EpisodeCommand initEpisodeCommand2;
    EpisodeCommand initEpisodeCommand3;

    SeriesCommand initSeriesCommand1;
    SeriesInfo initSeriesInfo1;

    SeriesCommand initSeriesCommand2;
    SeriesInfo initSeriesInfo2;

    EpisodesInfo initEpisodesInfo1;
    EpisodesInfo initEpisodesInfo2;
    EpisodesInfo initEpisodesInfo3;


    @BeforeEach
    void init() {

        initEpisodeCommand1 = new EpisodeCommand("tesztTitle", "Testopher Nolan");
        initEpisodesInfo1 = new EpisodesInfo(1, "tesztTitle", "Testopher Nolan", "Test of Thrones");

        initEpisodeCommand2 = new EpisodeCommand("tesztTitle2", "Testopher Nolan2");
        initEpisodesInfo2 = new EpisodesInfo(2, "tesztTitle2", "Testopher Nolan2", "Test of Thrones2");

        initEpisodeCommand3 = new EpisodeCommand("tesztTitle3", "Testopher Nolan3");
        initEpisodesInfo3 = new EpisodesInfo(3, "tesztTitle3", "Testopher Nolan3", "Test of Thrones3");


        initSeriesCommand1 = new SeriesCommand("tesztTitle", "Testopher Nolan");
        initSeriesInfo1 = new SeriesInfo(1, "tesztTitle", "Testopher Nolan", 2);

        initSeriesCommand2 = new SeriesCommand("tesztTitle2", "Testopher Nolan2");
        initSeriesInfo2 = new SeriesInfo(2, "tesztTitle2", "Testopher Nolan2", 2);
    }


    @Test
    @Order(1)
    void testSave_OneEpisode_Success() throws Exception {
        EpisodesInfo episodesInfo = restTemplate.postForObject("/api/series/1/episodes",
                initEpisodeCommand1, EpisodesInfo.class);
        assertThat(episodesInfo)
                .extracting(EpisodesInfo::getTitle)
                .isEqualTo(initEpisodesInfo1.getTitle());
//        mockMvc.perform(post("/api/series/1/episodes"), initEpisodeCommand1, EpisodesInfo.class )
//                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    void testFindAll_twoEpisodes_success() throws Exception {
        mockMvc.perform(get("/api/series/Allepisodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[4].title", equalTo("tesztTitle")));
    }


    @Test
    @Order(3)
    void testFindById_OneEpisode_Success() throws Exception {
        mockMvc.perform(get("/api/series/Allepisodes/5"))
                .andExpect(jsonPath("$.id", equalTo(5)))
                .andExpect(jsonPath("$.title", equalTo("tesztTitle")))
                .andExpect(jsonPath("$.director", equalTo("Testopher Nolan")));
    }

    @Test
    @Order(4)
    void testFindAllEpisodesForSeries_Success() throws Exception {
        mockMvc.perform(get("/api/series/1/episodes"))
                .andExpect(jsonPath("$[2].id", equalTo(5)))
                .andExpect(jsonPath("$[2].title", equalTo("tesztTitle")))
                .andExpect(jsonPath("$[2].director", equalTo("Testopher Nolan")));
    }

    @Test
    @Order(5)
    void testFindById_OneEpisode_Fail() throws Exception {
        mockMvc.perform(get("/api/series/Allepisodes/11"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", is("episode_id")))
                .andExpect(jsonPath("$[0].errorMessage", is("Episode with id 11 is not found.")));
    }

    @Test
    @Order(6)
    void testPutById_OneEpisode_success() throws Exception {
        restTemplate.put("/api/series/Allepisodes/5", initEpisodeCommand2, EpisodesInfo.class);
        mockMvc.perform(get("/api/series/Allepisodes/5"))
                .andExpect(jsonPath("$.title", equalTo(initEpisodesInfo2.getTitle())));
    }


    @Test
    @Order(7)
    void testDeleteById_OneEpisode_success() throws Exception {
        mockMvc.perform(delete("/api/series/Allepisodes/5"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @Order(8)
    void testDeleteById_OneEpisode_fail() throws Exception {
        mockMvc.perform(delete("/api/series/Allepisodes/5"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(9)
    void testGetArchiveList_success() throws Exception {
        mockMvc.perform(get("/api/archive/Alldeletedepisodes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", equalTo("tesztTitle2")));
    }
}
