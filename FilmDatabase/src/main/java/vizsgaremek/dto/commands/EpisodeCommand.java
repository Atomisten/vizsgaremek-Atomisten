package vizsgaremek.dto.commands;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.domain.Series;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeCommand {

    private String title;
    private String director;

}
