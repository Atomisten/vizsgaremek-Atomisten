package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodesInfo {

    private Integer id;
    private String title;
    private String director;
    private String seriesTitle;

}