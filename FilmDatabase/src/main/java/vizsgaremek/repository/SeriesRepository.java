package vizsgaremek.repository;

import org.springframework.stereotype.Repository;
import vizsgaremek.domain.Series;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SeriesRepository {

    @PersistenceContext
    private EntityManager entityManager;


    public Series save(Series toSave) {
        entityManager.persist(toSave);
        return toSave;
    }


    public List<Series> listAll() {
        return entityManager.createQuery("SELECT s FROM Series s", Series.class).getResultList();
    }

    public Series updateOrInsert(Series toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Series findById(Integer id) {
        return entityManager.createQuery("SELECT s FROM Series s WHERE s.id = :value", Series.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    public void deleteAllFromSeries(Integer id) {
        entityManager.createQuery("DELETE FROM Episodes WHERE series.id = :value")
                .setParameter("value", id)
                .executeUpdate();
    }


    public void delete(Series seriesToDelete) {
        entityManager.remove(seriesToDelete);
    }


}
