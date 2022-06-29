package vizsgaremek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoviesInfo {

    private Integer id;
    private String title;
    private String author;
    @Value("${cost_to_rent}")
    private int costToRent;

}
