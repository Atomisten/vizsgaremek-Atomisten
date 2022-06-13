package vizsgaremek.repository;

import org.springframework.stereotype.Repository;
import vizsgaremek.domain.Series;
import vizsgaremek.dto.archive.DeletedSeries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class SeriesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public DeletedSeries archive(DeletedSeries deletedEpisode) {
        entityManager.persist(deletedEpisode);
        return deletedEpisode;
    }

    public List<DeletedSeries> archiveList() {
        return entityManager.createQuery("SELECT d from DeletedSeries d", DeletedSeries.class).getResultList();
    }

    public Series save(Series toSave) {
        entityManager.persist(toSave);
        return toSave;
    }


    public List<Series> listAll() {
        return entityManager.createQuery("SELECT s FROM Series s", Series.class).getResultList();
    }

//    public List<Hive> findAll() {
//        return entityManager.createQuery("SELECT h FROM Hive h JOIN h.bees b WHERE b.pps = 88", Hive.class)
//                .getResultList();
//    }
//
//    public Hive findById(Integer id) {
//        return entityManager.find(Hive.class, id);
//    }

    public Series updateOrInsert(Series toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Series findByID(Integer id) {
        return entityManager.createQuery("SELECT s FROM Series s WHERE s.id = :value", Series.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    public void delete(Series movieToDelete) {
        entityManager.remove(movieToDelete);
    }
}
