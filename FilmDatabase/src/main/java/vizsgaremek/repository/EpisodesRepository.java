package vizsgaremek.repository;

import org.springframework.stereotype.Repository;
import vizsgaremek.domain.Episodes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class EpisodesRepository {

    @PersistenceContext
    private EntityManager entityManager;


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

    public Episodes findById(Integer id) {
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


}