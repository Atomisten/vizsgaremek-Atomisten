package vizsgaremek.dto.commands;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EpisodeCommand {

    @NotBlank(message = "cannot be blank or null")
    @Length(max = 255, message = "maximum 255 character")
    @Schema(description = "The title of the episode", example = "Winter Is Coming")
    private String title;

    @NotBlank(message = "cannot be blank or null")
    @Length(max = 255, message = "maximum 255 character")
    @Schema(description = "The director of the episode", example = "Steven Spielberg")
    private String director;

}
