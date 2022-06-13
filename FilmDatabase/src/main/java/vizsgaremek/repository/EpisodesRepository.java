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
        entityManager.persist(deletedEpisode);
        return deletedEpisode;
    }

    public List<DeletedEpisodes> archiveList() {
        return entityManager.createQuery("SELECT d from DeletedEpisodes d", DeletedEpisodes.class).getResultList();
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

    public void delete(Episodes movieToDelete) {
        entityManager.remove(movieToDelete);
    }

    public List<Episodes> listAllEpisodesForService(Integer id) {
        return entityManager.createQuery("SELECT e FROM Episodes e " +
                "WHERE e.series.id = :value ", Episodes.class)
                .setParameter("value", id)
                .getResultList();
    }
}