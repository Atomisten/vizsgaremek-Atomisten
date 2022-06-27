package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedSeriesInfo {

    private Integer id;
    private Integer seriesId;
    private String title;
    private String author;
    private int numberOfEpisodes;
}