package facades;

import entities.*;
import entities.Tool;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Purpose of this facade is to explore the CascadeType.All on relationship between Tool and Toy (applied on both sides)
 * 
 */
public class ToolFacade implements IDataFacade<Tool>{
    private static ToolFacade instance;
    private static EntityManagerFactory emf;

    //package private Constructor to ensure Singleton
    ToolFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static IDataFacade<Tool> getToolFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ToolFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public Tool create(Tool tool) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            // NOT WORKING: child is still persisted even when found first in db (if a toy exists with a specific name, we cannot persist this one. Rather get the one already there.
            tool.getToys().forEach((toy)->{
                try {
                    toy = em.createQuery("SELECT t FROM Toy t WHERE t.name = :name", Toy.class).setParameter("name",toy.getName()).getSingleResult();
                } catch (NoResultException e) {
                    em.persist(toy);
                }
            });
            em.persist(tool);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return tool;
    }

    @Override
    public Tool getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Tool t = em.find(Tool.class, id);
        if (t == null)
            throw new EntityNotFoundException("The RenameMe entity with ID: "+id+" Was not found");
        return t;
    }

    @Override
    public List<Tool> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Tool> query = em.createQuery("SELECT t FROM Tool t", Tool.class);
        List<Tool> Tools = query.getResultList();
        return Tools;
    }

    @Override
    public Tool update(Tool tool) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(tool);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return tool;
    }

    @Override
    public Tool delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Tool t = em.find(Tool.class, id);
        if (t == null)
            throw new EntityNotFoundException("The Tool entity with ID: "+id+" Was not found");
        try {
            em.getTransaction().begin();
            em.remove(t);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return t;
    }
}
