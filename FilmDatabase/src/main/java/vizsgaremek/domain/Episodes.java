package vizsgaremek.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "episodes")
public class Episodes {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "episode_id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "director")
    private String director;

    @ManyToOne
    private Series series;
}
