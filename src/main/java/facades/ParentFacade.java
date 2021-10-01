package facades;

import dtos.RenameMeDTO;
import entities.Parent;
import entities.RenameMe;
import errorhandling.EntityNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * created by THA
 * Purpose of this facade example is to show a facade used as a DB facade (only operating on entity classes - no DTOs
 * And to show case some different scenarios
 */
public class ParentFacade implements IDataFacade<Parent> {

    private static ParentFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ParentFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ParentFacade getParentFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ParentFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public Parent create(Parent p){
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return p;
    }

    @Override
    public Parent getById(int id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Parent p = em.find(Parent.class, id);
        if (p == null)
            throw new EntityNotFoundException("The RenameMe entity with ID: "+id+" Was not found");
        return p;
    }

    @Override
    public List<Parent> getAll(){
        EntityManager em = getEntityManager();
        TypedQuery<Parent> query = em.createQuery("SELECT p FROM Parent p", Parent.class);
        List<Parent> parents = query.getResultList();
        return parents;
    }

    @Override
    public Parent update(Parent parent) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Parent p = em.merge(parent);
        em.getTransaction().commit();
        return p;
    }

    @Override
    public Parent delete(int id) throws EntityNotFoundException{
        EntityManager em = getEntityManager();
        Parent p = em.find(Parent.class, id);
        if (p == null)
            throw new EntityNotFoundException("Could not remove Parent with id: "+id);
        em.getTransaction().begin();
        em.remove(p);
        em.getTransaction().commit();
        return p;
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        ParentFacade fe = getParentFacade(emf);
        fe.getAll().forEach(dto->System.out.println(dto));
    }


    //TODO Remove/Change this before use
    public long getRenameMeCount(){
        EntityManager em = getEntityManager();
        try{
            long renameMeCount = (long)em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        }finally{
            em.close();
        }
    }
}
