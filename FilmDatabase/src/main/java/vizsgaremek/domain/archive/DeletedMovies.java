package vizsgaremek.domain.archive;

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
@Table(name = "deleted_movies")
public class DeletedMovies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deleted_movie_id")
    private Integer id;

    @Column(name = "movie_id")
    private Integer movieId;

    @Column(name = "time_of_delete")
    private LocalDateTime timeOfDeletion;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "cost_to_rent")
    private int costToRent;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        DeletedMovies that = (DeletedMovies) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
