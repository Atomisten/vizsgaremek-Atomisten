package vizsgaremek.dto.archive;

import lombok.*;
import org.hibernate.Hibernate;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deleted_series")
public class DeletedSeries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deleted_series_id")
    private Integer id;

    @Column(name = "series_id")
    private Integer seriesId;

    @Column(name = "time_of_delete")
    private LocalDateTime localDateTime;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @OneToMany(mappedBy = "deletedSeries", fetch = FetchType.EAGER)
    private List<DeletedEpisodes> deletedEpisodesList;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeletedSeries that = (DeletedSeries) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}