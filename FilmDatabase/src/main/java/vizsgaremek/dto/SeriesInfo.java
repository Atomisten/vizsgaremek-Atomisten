package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeriesInfo {

    private Integer id;
    private String title;
    private String author;
    private int numberOfEpisodes;
}