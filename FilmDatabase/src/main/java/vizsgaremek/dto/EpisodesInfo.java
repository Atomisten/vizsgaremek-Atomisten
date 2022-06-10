package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.domain.Series;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodesInfo {

    private Integer id;
    private String title;
    private String author;
    private Series series;

}