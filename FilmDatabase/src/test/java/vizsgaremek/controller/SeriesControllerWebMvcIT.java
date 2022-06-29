package vizsgaremek.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import vizsgaremek.dto.SeriesInfo;
import vizsgaremek.service.MoviesService;
import vizsgaremek.service.SeriesService;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SeriesController.class)
public class SeriesControllerWebMvcIT {

    @MockBean
    SeriesService seriesService;

    @Autowired
    MockMvc mockMvc;

    @Test
    void testFindAll_twoSeries_success() throws Exception {
        when(seriesService.listAllSeries())
                .thenReturn(List.of(
                        new SeriesInfo(1, "tesztTitle", "tesztAuthor", 1),
                        new SeriesInfo(2, "2ndtesztTitle", "2ndtesztAuthor", 1)
                ));
        mockMvc.perform(get("/api/Allseries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].title", equalTo("2ndtesztTitle")));
    }
}
