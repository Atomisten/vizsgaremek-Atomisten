package vizsgaremek.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.dto.commands.SeriesCommand;
import vizsgaremek.service.SeriesService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SeriesControllerRestTemplateIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestRestTemplate restTemplate;

    SeriesCommand initSeriesCommand1;
    SeriesCommand initSeriesCommand2;
    SeriesCommand initSeriesCommand3;

    SeriesInfo initSeriesInfo1;
    SeriesInfo initSeriesInfo2;
    SeriesInfo initSeriesInfo3;


    @BeforeEach
    void init() {
        initSeriesCommand1 = new SeriesCommand("tesztTitle", "Testopher Nolan");
        initSeriesInfo1 = new SeriesInfo(1, "tesztTitle", "Testopher Nolan", 2);

        initSeriesCommand2 = new SeriesCommand("tesztTitle2", "Testopher Nolan2");
        initSeriesInfo2 = new SeriesInfo(2, "tesztTitle2", "Testopher Nolan2", 2);

        initSeriesCommand3 = new SeriesCommand("tesztTitle3", "Testopher Nolan3");
        initSeriesInfo3 = new SeriesInfo(3, "tesztTitle3", "Testopher Nolan3", 2);
    }




    @Test
    @Order(1)
    void testSave_OneSeries_Success() {
        SeriesInfo seriesInfo = restTemplate.postForObject("/api/series",
                initSeriesCommand1, SeriesInfo.class);
        assertThat(seriesInfo)
                .extracting(SeriesInfo::getTitle)
                .isEqualTo(initSeriesInfo1.getTitle());
    }
    @Test
    @Order(2)
    void testFindAll_twoSeries_success() throws Exception {
        mockMvc.perform(get("/api/Allseries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[2].title", equalTo("tesztTitle")));
    }
    @Test
    @Order(3)
    void testFindById_OneSeries_Success() throws Exception {
        mockMvc.perform(get("/api/series/3"))
                .andExpect(jsonPath("$.title", equalTo(initSeriesInfo1.getTitle())))
                .andExpect(jsonPath("$.id", equalTo(3)))
                .andExpect(jsonPath("$.author", equalTo(initSeriesInfo1.getAuthor())));
    }

    @Test
    @Order(4)
    void testFindById_OneSeries_Fail() throws Exception {
        mockMvc.perform(get("/api/series/11"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$[0].field", is("series_id")))
                .andExpect(jsonPath("$[0].errorMessage", is("Series with id 11 is not found.")));
    }

//    @Test
//    @Order(5)
//    void testPutById_OneSeries_success() throws Exception {
//        restTemplate.put("/api/series/3", initSeriesCommand2, SeriesInfo.class);
//        mockMvc.perform(get("/api/series/3"))
//                .andExpect(jsonPath("$.title", equalTo(initSeriesInfo2.getTitle())));
//    }


    @Test
    @Order(6)
    void testDeleteById_OneSeries_success() throws Exception {
        mockMvc.perform(delete("/api/series/3"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @Order(7)
    void testGetArchiveList_success() throws Exception {
        mockMvc.perform(get("/api/archive/Alldeletedseries"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", equalTo("tesztTitle")));
    }

    @Test
    @Order(8)
    void testPurge() throws Exception {
        mockMvc.perform(delete("/api/archive/deletedseries/PURGE"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(9)
    void testArchivedDelete_fail() throws Exception {
        mockMvc.perform(delete("/api/archive/deletedseries/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(10)
    void testArchivedPurge() throws Exception {
        mockMvc.perform(delete("/api/archive/deletedepisodes/PURGE"))
                .andExpect(status().isOk());
    }
}
