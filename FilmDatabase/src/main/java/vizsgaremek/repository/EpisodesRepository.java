package vizsgaremek.repository;

import org.springframework.stereotype.Repository;
import vizsgaremek.domain.Episodes;
import vizsgaremek.dto.archive.DeletedEpisodes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EpisodesRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public DeletedEpisodes archive(DeletedEpisodes deletedEpisode) {
        return entityManager.merge(deletedEpisode);

    }

    public void flush(){
        entityManager.flush();
    }

    public List<DeletedEpisodes> archiveList() {
        return entityManager.createQuery("SELECT de from DeletedEpisodes de", DeletedEpisodes.class).getResultList();
    }

    public Episodes save(Episodes toSave) {
        entityManager.persist(toSave);
        return toSave;
    }


    public List<Episodes> listAll() {
        return entityManager.createQuery("SELECT e FROM Episodes e", Episodes.class).getResultList();
    }


    public Episodes updateOrInsert(Episodes toUpdate) {
        return entityManager.merge(toUpdate);
    }

    public Episodes findByID(Integer id) {
        return entityManager.createQuery("SELECT e FROM Episodes e WHERE e.id = :value", Episodes.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    public void delete(Episodes episodeToDelete) {
        entityManager.remove(episodeToDelete);
    }

    public List<Episodes> listAllEpisodesForService(Integer id) {
        return entityManager.createQuery("SELECT e FROM Episodes e " +
                "WHERE e.series.id = :value ", Episodes.class)
                .setParameter("value", id)
                .getResultList();
    }

    public DeletedEpisodes archivedFindByID(Integer id) {
        return entityManager.createQuery("SELECT de FROM DeletedEpisodes de WHERE de.id = :value", DeletedEpisodes.class)
                .setParameter("value", id)
                .getSingleResult();
    }

    public void deleteArchiveEpisode(DeletedEpisodes archivedToDelete ) {
        entityManager.remove(archivedToDelete);
    }

    public void purge() {
        entityManager.createQuery("DELETE FROM DeletedEpisodes de").executeUpdate();
    }

    public DeletedEpisodes archiveP(DeletedEpisodes deletedEpisodeToSave) {
        entityManager.persist(deletedEpisodeToSave);
        return deletedEpisodeToSave;
    }
}