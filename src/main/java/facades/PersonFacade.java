package facades;

import dtos.HobbyDTO;
import dtos.PersonDTO;
import dtos.RenameMeDTO;
import entities.Hobby;
import entities.Person;
import entities.RenameMe;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public PersonDTO create(PersonDTO pdto){
        Person p = new Person(pdto.getName(), pdto.getAge());
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(p);
    }

    public HobbyDTO createHobby(HobbyDTO hdto){
        Hobby h = new Hobby(hdto.getName(), hdto.getDescription());
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(h);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new HobbyDTO(h);
    }

    public boolean removeHobbyFromPerson(Long personId, Long hobbyId){
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, personId);
        Hobby h = em.find(Hobby.class, hobbyId);
        if(p == null || h == null)
            throw new IllegalArgumentException("Person or Hobby not found");
        p.removeHobby(h);
        try {
            em.getTransaction().begin();
            em.merge(p);
//            em.merge(h)
            em.getTransaction().commit();
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
        return true;
    }


    public PersonDTO addHobby(Long personId, Long hobbyId){
        EntityManager em = getEntityManager();
        Person p = em.find(Person.class, personId);
        Hobby h = em.find(Hobby.class, hobbyId);
        if(p == null || h == null)
            throw new IllegalArgumentException("Person or Hobby not found");
        p.addHobby(h);
        try {
            em.getTransaction().begin();
            em.merge(p);
//            em.merge(h);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PersonDTO(p);
    }

    public PersonDTO getById(long id) { //throws RenameMeNotFoundException {
        EntityManager em = emf.createEntityManager();
        Person rm = em.find(Person.class, id);
//        if (rm == null)
//            throw new RenameMeNotFoundException("The RenameMe entity with ID: "+id+" Was not found");
        return new PersonDTO(rm);
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
    
    public List<PersonDTO> getAll(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> rms = query.getResultList();
        return PersonDTO.getDtos(rms);
    }
    
    public static void main(String[] args) {
        emf = EMF_Creator.createEntityManagerFactory();
        PersonFacade fe = getPersonFacade(emf);

        fe.getAll().forEach(dto->System.out.println(dto));
    }

}
