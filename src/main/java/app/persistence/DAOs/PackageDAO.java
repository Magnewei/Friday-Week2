package app.persistence.DAOs;

import app.persistence.entities.Package;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

public class PackageDAO implements iDAO<Package> {
    private final EntityManagerFactory emf;

    public PackageDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public boolean create(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(pack);
            em.getTransaction().commit();
            return true;
        }
    }

    @Override
    public boolean delete(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(pack);
            em.getTransaction().commit();
            return true;
        }
    }

    @Override
    public Package getById(long id) {
        Package pack;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            pack = em.createQuery("SELECT p FROM Package p WHERE p.id = :id", Package.class)
                    .setParameter("id", id)
                    .getSingleResult();

            em.persist(pack);
            em.getTransaction().commit();
            return pack;
        }
    }

    @Override
    public Set<Package> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p", Package.class);
            em.getTransaction().commit();
            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public boolean update(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(pack);
            em.getTransaction().commit();
            return true;
        }
    }
}
