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

    public DeletedSeries archive(DeletedSeries deletedSeries) {
        return entityManager.merge(deletedSeries);
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

    public Series updateOrInsert(Series toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Series findByID(Integer id) {
        return entityManager.createQuery("SELECT s FROM Series s WHERE s.id = :value", Series.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    public DeletedSeries ArchivefindBySeriesId(Integer seriesId) {
        return entityManager.createQuery("SELECT ds FROM DeletedSeries ds WHERE ds.seriesId = :value", DeletedSeries.class)
                .setParameter("value", seriesId)
                .getSingleResult();
    }

    public void delete(Series seriesToDelete) {
        entityManager.remove(seriesToDelete);
    }

    public void deleteAllFromSeries(Integer id) {
        entityManager.createQuery("DELETE FROM Episodes WHERE series.id = :value")
                .setParameter("value", id)
                .executeUpdate();
    }
}
