package datafacades;

import entities.Child;
import entities.Toy;
import errorhandling.EntityNotFoundException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

public class ChildrenFacade implements IDataFacade<Child>{

    private static ChildrenFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ChildrenFacade() {}

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static IDataFacade<Child> getFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ChildrenFacade();
        }
        return instance;
    }


    @Override
    public Child create(Child child) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            child.getToys().forEach(el->{
                if(el.getId()!=0)
                    el = em.find(Toy.class, el.getId());
                else
                    em.persist(el);
            });
            em.persist(child);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return child;
    }


    public Child createWithMerge(Child child) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            child.getToys().forEach(el->el = em.merge(el));
            child = em.merge(child); //merge rather than persist will create new if not exist and update if exist.
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return child;
    }

    @Override
    public Child getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Child child = em.find(Child.class, id);
        if (child == null)
            throw new EntityNotFoundException("The Child entity with ID: "+id+" Was not found");
        return child;
    }

    @Override
    public List<Child> getAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Child> query = em.createQuery("SELECT c FROM Child c", Child.class);
        List<Child> children = query.getResultList();
        return children;
    }

    @Override
    public Child update(Child child) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Child c = em.merge(child);
        em.getTransaction().commit();
        return c;
    }

    @Override
    public Child delete(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Child c = em.find(Child.class, id);
        if (c == null)
            throw new EntityNotFoundException("Could not remove Child with id: "+id);
        em.getTransaction().begin();
        em.remove(c);
        em.getTransaction().commit();
        return c;
    }
}
