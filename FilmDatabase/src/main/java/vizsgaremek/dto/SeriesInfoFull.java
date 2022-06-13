package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeriesInfoFull {

    private Integer id;
    private String title;
    private String author;
    private List<EpisodesInfoFull> episodesInfoFull;
}