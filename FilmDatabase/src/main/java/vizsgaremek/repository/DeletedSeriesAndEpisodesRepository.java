package vizsgaremek.repository;

import org.springframework.stereotype.Repository;
import vizsgaremek.domain.archive.DeletedEpisodes;
import vizsgaremek.domain.archive.DeletedSeries;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class DeletedSeriesAndEpisodesRepository {


    @PersistenceContext
    private EntityManager entityManager;


    public DeletedEpisodes archiveEpisode(DeletedEpisodes deletedEpisode) {
        return entityManager.merge(deletedEpisode);
    }

    public DeletedSeries archiveSeries(DeletedSeries deletedSeries) {
        return entityManager.merge(deletedSeries);
    }

    public void purgeEpisodes() {
        entityManager.createQuery("DELETE FROM DeletedEpisodes de").executeUpdate();
    }

    public void purgeSeries() {
        entityManager.createQuery("DELETE FROM DeletedSeries ds").executeUpdate();
    }

    public DeletedSeries archivedSeriesFindById(Integer seriesId) {
        return entityManager.createQuery("SELECT ds FROM DeletedSeries ds WHERE ds.seriesId = :value", DeletedSeries.class)
                .setParameter("value", seriesId)
                .getSingleResult();
    }

    public DeletedEpisodes archivedEpisodeFindById(Integer id) {
        return entityManager.createQuery("SELECT de FROM DeletedEpisodes de WHERE de.id = :value", DeletedEpisodes.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    public void deleteAllEpisodesFromArchivedSeries(Integer id) {
        entityManager.createQuery("DELETE FROM DeletedEpisodes Where deletedSeries.id = :value")
                .setParameter("value", id)
                .executeUpdate();
    }

    public void deleteArchivedSeries(DeletedSeries deletedSeries) {
        entityManager.remove(deletedSeries);
    }

    public void deleteArchiveEpisode(DeletedEpisodes archivedToDelete) {
        entityManager.remove(archivedToDelete);
    }

    public List<DeletedEpisodes> deletedEpisodeList() {
        return entityManager.createQuery("SELECT de from DeletedEpisodes de", DeletedEpisodes.class).getResultList();
    }

    public List<DeletedSeries> deletedSeriesList() {
        return entityManager.createQuery("SELECT d from DeletedSeries d", DeletedSeries.class).getResultList();
    }
}