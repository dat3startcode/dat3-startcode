package facades;
import entities.Child;
import entities.IdentificationCard;
import entities.Parent;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class Exercise1Test {
    private static EntityManagerFactory emf;
    private static IDataFacade<Parent> facade;
    Parent p1,p2;
    IdentificationCard i1, i2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ParentFacade.getParentFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
        emf.close();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("IdentificationCard.deleteAllRows").executeUpdate();
            em.createNamedQuery("Toy.deleteAllRows").executeUpdate();
            em.createNamedQuery("Child.deleteAllRows").executeUpdate();
            em.createNamedQuery("Parent.deleteAllRows").executeUpdate();
            p1 = new Parent("Daddy", 55);
            p2 = new Parent("Mommy", 50);
            i1 = new IdentificationCard(IdentificationCard.IdentificationType.DRIVERS_LICENS,"3333333333","Should be renewed soon");
            i2 = new IdentificationCard(IdentificationCard.IdentificationType.PASSPORT,"23232323","Password text");

            p1.addCard(i1);
            p1.addCard(i2);
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
//        emf.close();
    }
    @Test
    public void removeParentWithIDs(){

    }


}
