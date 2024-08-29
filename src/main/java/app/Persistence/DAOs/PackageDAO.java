package app.Persistence.DAOs;

import app.Persistence.Entities.Package;
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

    public boolean deleteById(int id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE Package p WHERE p.id = :id").setParameter("id", id).executeUpdate();
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

    public Package getByTrackingNumber(int id) {
        Package pack;

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            pack = em.createQuery("SELECT p FROM Package p WHERE p.trackingNumber = :id", Package.class)
                    .setParameter("id", id)
                    .getSingleResult();

            em.persist(pack);
            em.getTransaction().commit();
            return pack;
        }
    }

    public boolean updateDeliveryStatus(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // ++ the delivery status of the package.
            pack.setDeliveryStatus(pack.getDeliveryStatus().getNextStatus());

            em.merge(pack);
            em.getTransaction().commit();
            return true;
        }
    }
}
