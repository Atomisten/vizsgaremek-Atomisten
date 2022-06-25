package vizsgaremek.dto.archive;

import lombok.*;
import org.hibernate.Hibernate;
import vizsgaremek.domain.Series;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deleted_episodes")
public class DeletedEpisodes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deleted_episode_id")
    private Integer id;

    @Column(name = "episode_id")
    private Integer episodeId;

    @Column(name = "time_of_delete")
    private LocalDateTime localDateTime;

    @Column(name = "title")
    private String title;

    @Column(name = "director")
    private String director;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "deleted_series_id")
    private DeletedSeries deletedSeries;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeletedEpisodes that = (DeletedEpisodes) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
