package vizsgaremek.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.dto.EpisodesInfo;
import vizsgaremek.service.EpisodesService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = EpisodesController.class)
public class EpisodesControllerWebMvcIT {

    @MockBean
    EpisodesService episodesService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testFindAll_twoEpisodes_success() throws Exception {
        when(episodesService.listAllEpisodes())
                .thenReturn(List.of(
                        new EpisodesInfo(1, "tesztTitle", "tesztAuthor", "Game of Thrones"),
                        new EpisodesInfo(2, "2ndtesztTitle", "2ndtesztAuthor", "Game of Thrones")
                ));
        mockMvc.perform(get("/api/series/Allepisodes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].title", equalTo("2ndtesztTitle")));
    }


}