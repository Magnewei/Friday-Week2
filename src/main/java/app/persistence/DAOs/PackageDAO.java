package app.persistence.DAOs;

import app.persistence.entities.Package;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

public class PackageDAO implements iDAO<Package> {
    private final EntityManagerFactory emf;

    public PackageDAO(EntityManagerFactory entityManagerFactory) {
        emf = entityManagerFactory;
    }

    @Override
    public boolean create(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(pack);
            em.getTransaction().commit();
            return true;

        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            emf.createEntityManager().getTransaction().rollback();
            emf.close();
        }

        return false;
    }

    @Override
    public boolean delete(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.remove(pack);
            em.getTransaction().commit();
            return true;

        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            emf.createEntityManager().getTransaction().rollback();
            emf.close();
        }

        return false;
    }

    @Override
    public Package getById(Package pack) {

        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.find(Package.class, pack.getId());
            em.getTransaction().commit();
            return pack;

        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            emf.createEntityManager().getTransaction().rollback();
            emf.close();
        }

        return null;
    }

    @Override
    public Set<Package> getAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Package> query = em.createQuery("SELECT p FROM Package p", Package.class);
            return new HashSet<>(query.getResultList());

        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            emf.createEntityManager().getTransaction().rollback();
            emf.close();
        }

        return null;
    }

    @Override
    public boolean update(Package pack) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.merge(pack);
            em.getTransaction().commit();
            return true;

        } catch (IllegalArgumentException | IllegalStateException e) {
            e.printStackTrace();
            emf.createEntityManager().getTransaction().rollback();
            emf.close();
        }

        return false;
    }
}
