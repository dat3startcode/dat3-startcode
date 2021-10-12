package datafacades;

import entities.Child;
import entities.Toy;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;

import static org.junit.jupiter.api.Assertions.*;

class ChildrenFacadeTest {

    private static EntityManagerFactory emf;
//    private static IDataFacade<Child> facade; //This is the right way, but I need alternative method implementations to illustrate points
    private static IDataFacade<Child> facade;
    Child c1,c2;
    Toy t1,t2;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ChildrenFacade.getFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Child.deleteAllRows").executeUpdate();
            em.createNamedQuery("Toy.deleteAllRows").executeUpdate();

            c1 = new Child("Dorthea", 3);
            c2 = new Child("Frederik", 6);
            t1 = new Toy("Lego batman set",3);
            t2 = new Toy("Doll house",4);

            em.persist(c1);
            em.persist(c2);
            em.persist(t1);
            em.persist(t2);
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
    void create() {
        System.out.println("Testing create(Child p) should get id from db");
        Child c = new Child("TestChild",10);
        Child actual   = facade.create(c);
        assertTrue(actual.getId()!=0);
    }

    @Test
    void createWithNewToys() {
        System.out.println("Testing create(Child c) with new toys added. Child has CascadeType.PERSIST to Toy");
        Child c = new Child("TestChild",10);
        c.addToy(new Toy("Balloon castle",10));
        c.addToy(new Toy("Chess board",8));
        facade.create(c);
        int expected = 4;
        long actual = (Long) emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM TOY").getSingleResult();
        assertEquals(expected, actual);
    }

    @Test
    void createWithPersist() {
        System.out.println("Testing create(Child c) with new toys added. 2 toys should be persisted along with the child");
        Child c = new Child("TestChild",10);
        c.addToy(new Toy("Balloon castle",10));
        c.addToy(new Toy("Chess board",8));
        facade.create(c);
        int expected = 4;
        long actual = (Long) emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM TOY").getSingleResult();
        assertEquals(expected, actual);
    }

    @Test
    void createWithNewToys2() {
        System.out.println("Testing create(Child c) with toys added and check if toys are in fact added");
        Child c = new Child("TestChild",10);
        c.addToy(new Toy("Ballon castle",10));
        c.addToy(new Toy("Chess board",8));
        List<Toy> result   = facade.create(c).getToys();
        assertThat(result, containsInAnyOrder(
                hasProperty("name", is("Ballon castle")),
                hasProperty("name", is("Chess board"))
        ));
    }

    @Test
    void createWithKnownToys() {
        System.out.println("Testing create(Child p) with children added, that No duplicates are created");
        Child c = new Child("TestChild",10);
        c.addToy(t1);
        c.addToy(t2);
        c = facade.create(c);
        int expected = 2;
        long actual = (Long) emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM TOY").getSingleResult();
        assertEquals(expected, actual);
        assertTrue(c.getToys().size()==2);
    }

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Child expected = c1;
        Child actual = facade.getById(c1.getId());
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
        System.out.println("Testing Update(Child c)");
        c2.setAge(65);
        Child expected = c2;
        Child actual = facade.update(c2);
        assertEquals(expected,actual);
    }

    @Test
    void updateWithChildren() throws EntityNotFoundException {
        System.out.println("Testing Update(Child p) with known children using merge");
        c2.addToy(t1);
        c2.addToy(t2);
        Child c = facade.update(c2);
        int expected = 2;
        int actual = c.getToys().size();
        assertEquals(expected,actual);
        actual = (int)(long) emf.createEntityManager().createNativeQuery("SELECT COUNT(*) FROM TOY").getSingleResult();
        assertEquals(expected, actual,"Testing for duplicate toys");
    }

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id)");
        Child c = facade.delete(c1.getId());
        int expected = 1;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
        assertEquals(c,c1);
    }
}