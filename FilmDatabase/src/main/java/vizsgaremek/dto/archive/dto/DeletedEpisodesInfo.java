package vizsgaremek.dto.archive.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletedEpisodesInfo {

    private Integer id;
    private Integer episodeId;
    private String title;
    private String director;
    private LocalDateTime localDateTime;
    private String seriesTitle;
    private Integer seriesId;
}