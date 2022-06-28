package vizsgaremek.dto.commands;


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
    private String title;

    @NotBlank(message = "cannot be blank or null")
    @Length(max = 255, message = "maximum 255 character")
    private String director;

}
