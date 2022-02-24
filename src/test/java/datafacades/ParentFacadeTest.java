package datafacades;

import entities.Child;
import entities.Parent;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

class ParentFacadeTest {

    private static EntityManagerFactory emf;
    private static IDataFacade<Parent> facade;
    Parent p1,p2;
    Child c1,c2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ParentFacade.getParentFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test

    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Toy.deleteAllRows").executeUpdate();
            em.createNamedQuery("Child.deleteAllRows").executeUpdate();
            em.createNamedQuery("Parent.deleteAllRows").executeUpdate();
            p1 = new Parent("Daddy", 55);
            p2 = new Parent("Mommy", 50);
            c1 = new Child("Dorthea", 3);
            c2 = new Child("Frederik", 6);
            em.persist(p1);
            em.persist(p2);
            em.persist(c1);
            em.persist(c2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
    }


    @Test
    void create() {
        System.out.println("Testing create(Parent p)");
        Parent p = new Parent("TestParent",10);
        Parent expected = p;
        Parent actual   = facade.create(p);
        assertEquals(expected, actual);
    }

    @Test
    void createWithChildren() {
        System.out.println("Testing create(Parent p) with children added");
        Parent p = new Parent("TestParent",10);
        p.addChild(new Child("Alfred",10));
        p.addChild(new Child("Roberta",8));
        Parent expected = p;
        Parent actual   = facade.create(p);
        assertEquals(expected, actual);
    }

    @Test
    void createWithKnownChildren() {
        System.out.println("Testing create(Parent p) with children added");
        Parent p = new Parent("TestParent",10);
        p.addChild(c1);
        p.addChild(c2);
        Parent expected = p;
        Parent actual   = facade.create(p);
        assertEquals(expected, actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Parent expected = p1;
        Parent actual = facade.getById(p1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 2;
        int actual = facade.getAll().size();
        assertEquals(expected,actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        System.out.println("Testing Update(Parent p)");
        p2.setAge(65);
        Parent expected = p2;
        Parent actual = facade.update(p2);
        assertEquals(expected,actual);
    }

    @Test
    void updateWithChildren() throws EntityNotFoundException {
        System.out.println("Testing Update(Parent p) with known children");
        p2.addChild(c1);
        p2.addChild(c2);
        Parent p = facade.update(p2);
        int expected = 2;
        int actual = p.getChildren().size();
        assertEquals(expected,actual);
    }

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id)");
        Parent p = facade.delete(p1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(p,p1);
    }
}