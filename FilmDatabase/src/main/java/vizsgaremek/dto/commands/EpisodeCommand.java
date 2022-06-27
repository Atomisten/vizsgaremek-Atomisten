package vizsgaremek.dto.commands;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import vizsgaremek.domain.Series;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeCommand {

    @NotBlank(message = "cannot be blank or null")
    @Max(value = 255, message = "maximum 255 character")
    private String title;

    @NotBlank(message = "cannot be blank or null")
    @Max(value = 255, message = "maximum 255 character")
    private String director;

}
