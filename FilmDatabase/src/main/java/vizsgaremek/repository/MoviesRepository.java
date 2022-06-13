package vizsgaremek.repository;

import org.springframework.stereotype.Repository;
import vizsgaremek.domain.Movies;
import vizsgaremek.dto.archive.DeletedMovies;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MoviesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public DeletedMovies archive(DeletedMovies deletedMovie) {
        entityManager.persist(deletedMovie);
        return deletedMovie;
    }

    public List<DeletedMovies> archiveList() {
        return entityManager.createQuery("SELECT d from DeletedMovies d", DeletedMovies.class).getResultList();
    }

    public Movies save(Movies toSave) {
        entityManager.persist(toSave);
        return toSave;
    }


    public List<Movies> listAll() {
        return entityManager.createQuery("SELECT m FROM Movies m", Movies.class).getResultList();
    }


    public Movies updateOrInsert(Movies toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Movies findByID(Integer id) {
        return entityManager.createQuery("SELECT m FROM Movies m WHERE m.id = :value", Movies.class)
                .setParameter("value", id)
                .getSingleResult();
    }

//    public Movies findByID(Integer id) {
//        return entityManager.find(Movies.class, id);
//    }

    public void delete(Movies movieToDelete) {
        entityManager.remove(movieToDelete);
    }

//    public void delete (Optional<Movies> movieToDelete) {
//        entityManager.remove(movieToDelete);
//    }
//
//    public Optional<Movies> findByID(Integer id) {
//        return Optional.of(entityManager.createQuery("SELECT m FROM Movies m WHERE m.id = :value", Movies.class)
//                .setParameter("value", id)
//                .getSingleResult());
//    }
}