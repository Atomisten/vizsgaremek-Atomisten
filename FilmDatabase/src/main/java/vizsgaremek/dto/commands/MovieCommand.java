package vizsgaremek.dto.commands;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieCommand {

    @NotBlank(message = "cannot be blank or null")
    @Max(value = 255, message = "maximum 255 character")
    private String title;

    @NotBlank(message = "cannot be blank or null")
    @Max(value = 255, message = "maximum 255 character")
    private String author;

}
