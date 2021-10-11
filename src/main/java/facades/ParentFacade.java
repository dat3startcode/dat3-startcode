package facades;

import entities.Child;
import entities.Parent;
import entities.Toy;
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

    //package private Constructor to ensure Singleton
    ParentFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static IDataFacade<Parent> getParentFacade(EntityManagerFactory _emf) {
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
            p.getChildren().forEach(child->{
                if(child.getId()!=0)
                    child = em.find(Child.class,child.getId());
                else {
                    child.getToys().forEach(toy->{
                        if(toy.getId()!=0)
                            toy = em.find(Toy.class, toy.getId());
                        else {
                            em.persist(toy);
                        }
                    });
                    em.persist(child);
                }
            });
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
            parent.getChildren().forEach(child->{
                if(child.getId()!=0)
                    em.merge(child);
                else
                    em.persist(child);
            });
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
        // remove dangling children
        p.getChildren().forEach(child->{
            if(child.getId()!=0)
                //detach toys from child before removing child
                child.getToys().forEach(toy->{
                    if(toy.getId()!=0){
                        toy.getChildren().remove(child);
                        em.merge(toy);
                    }

                });
                em.remove(child);
        });
        em.remove(p);
        em.getTransaction().commit();
        return p;
    }

    /**
     * Alternative delete method that does not remove children
     * @param id
     * @return
     * @throws EntityNotFoundException
     */
    public Parent deleteWithoutCascading(int id) throws EntityNotFoundException{
        EntityManager em = getEntityManager();
        Parent p = em.find(Parent.class, id);
        if (p == null)
            throw new EntityNotFoundException("Could not remove Parent with id: "+id);
        em.getTransaction().begin();
        // detach dangling children
        p.getChildren().forEach(child->{
            if(child.getId()!=0)
                child.setParent(null);
                em.merge(child);
        });
        em.remove(p);
        em.getTransaction().commit();
        return p;
    }

    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        IDataFacade fe = getParentFacade(emf);
        fe.getAll().forEach(dto->System.out.println(dto));
    }


    //TODO Remove/Change this before use
    public long count(){
        EntityManager em = getEntityManager();
        try{
            return (long)em.createQuery("SELECT COUNT(p) FROM Parent p").getSingleResult();
        }finally{
            em.close();
        }
    }
}
