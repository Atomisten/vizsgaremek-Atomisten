package vizsgaremek.dto.archive;

import lombok.*;
import org.hibernate.Hibernate;

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
    @Column(name = "deletion_id")
    private Integer deleteId;

//    @Column(name = "date_time")
//    private LocalDateTime localDateTime;                                                        //TODO törlés dátum

    @Column(name = "movie_id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeletedEpisodes that = (DeletedEpisodes) o;
        return deleteId != null && Objects.equals(deleteId, that.deleteId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
