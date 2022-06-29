package vizsgaremek.dto.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieCommand {

    @NotBlank(message = "cannot be blank or null")
    @Length(max = 255, message = "maximum 255 character")
    @Schema(description = "The title of the movie", example = "The Shining")
    private String title;

    @NotBlank(message = "cannot be blank or null")
    @Length(max = 255, message = "maximum 255 character")
    @Schema(description = "The original author of the movie", example = "Stephen King")
    private String author;

}
