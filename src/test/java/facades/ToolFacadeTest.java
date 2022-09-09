package facades;

import entities.Tool;
import entities.Toy;
import errorhandling.EntityNotFoundException;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled
class ToolFacadeTest {
    private static EntityManagerFactory emf;
    private static IDataFacade<Tool> facade;
    Tool tool1, tool2, tool3, tool4;
    Toy toy1, toy2, toy3, toy4;

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ToolFacade.getToolFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
        emf.close();
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Toy.deleteAllRows").executeUpdate();
            em.createNamedQuery("Tool.deleteAllRows").executeUpdate();
            tool1 = new Tool("Wrench", 55, 2.00);
            tool2 = new Tool("Hammer", 50, 10.00);
            tool3 = new Tool("Pliers", 3, 33.00);
            tool4 = new Tool("Screwdriver", 6, 1.00);

            toy1 = new Toy("Teddybear",1,3.00);
            toy2 = new Toy("Chess board",2,3.00);
            toy3 = new Toy("Pool table",3,50.00);
            toy4 = new Toy("Motocross bike",4,300.00);
            tool1.addToy(toy1);
            tool2.addToy(toy2);
            toy2.addTool(tool1);
            em.persist(tool1);
            em.persist(tool2);
            em.persist(tool3);
            em.persist(tool4);
            em.persist(toy3); //toy 3 and 4 are persisted alone
            em.persist(toy4);
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
        System.out.println("Testing create(Tool p)");
        Tool t = new Tool("TestTool", 10, 2.00);
        Tool expected = t;
        Tool actual = facade.create(t);
        assertEquals(expected, actual);
    }

    @Test
    void createWithToys() {
        System.out.println("Testing create(Tool t) with tools added");
        Tool t = new Tool("TestTool", 10, 3.00);
        t.addToy(new Toy("Sling shot", 10, 0.00));
        t.addToy(new Toy("Dart", 8, 10.00));
        Tool expected = t;
        Tool actual = facade.create(t);
        assertEquals(expected, actual);
        Toy actualToy = emf.createEntityManager().createQuery("SELECT t FROM Toy t WHERE t.name = 'Sling shot'", Toy.class).getSingleResult();
        assertTrue(actualToy.getTools().contains(t));
    }

    @Test
    void createWithKnownToys() {
        System.out.println("Testing create(Tool p) with tools added. The CascadeType.Persist will result in toy3 trying to be persisted again. Toy.name has a unique constraint that makes this impossible. Solution: DONT use Cascade with non indentifying relationships");
        Tool t = new Tool("TestTool", 10, 2.00);
        t.addToy(toy3);
        Tool expected = t;
        assertThrows(javax.persistence.RollbackException.class, ()->facade.create(t));

    }

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Tool expected = tool1;
        Tool actual = facade.getById(tool1.getId());
        assertEquals(expected, actual);
    }

    @Test
    void getAll() {
        System.out.println("Testing getAll()");
        int expected = 4;
        int actual = facade.getAll().size();
        assertEquals(expected, actual);
    }

    @Test
    void update() throws EntityNotFoundException {
        System.out.println("Testing Update(Tool p)");
        tool2.setAge(65);
        Tool expected = tool2;
        Tool actual = facade.update(tool2);
        assertEquals(expected, actual);
    }

    @Test
    void updateWithToys() throws EntityNotFoundException {
        System.out.println("Testing Update(Tool p) with known toys without getting duplicate");
        tool3.addToy(toy3);
        Tool p = facade.update(tool3);
        //assert no duplicates
        int expectedNumberOfToys = 4;
        int actualNumberOfToys = (int) (long) emf.createEntityManager().createNativeQuery("SELECT COUNT(id) FROM TOY").getSingleResult();
        assertEquals(expectedNumberOfToys, actualNumberOfToys);
        //assert bidirectional relationship
        Toy toy = emf.createEntityManager().find(Toy.class, toy3.getId());
        assertTrue(toy.getTools().contains(tool3));
    }

    @Test
    void updateWithNewToys() throws EntityNotFoundException {
        System.out.println("Testing Update(Tool p) with known tools");
        tool3.addToy(new Toy("Gummi bear", 12, 0.02));
        tool3.addToy(new Toy("Looking glass", 2, 1.00));
        Tool t = facade.update(tool3);
        int expected = 2;
        int actual = t.getToys().size();
        //assert that toys are added to db
        assertEquals(expected, actual);
        int expectedNumberOfToys = 6;
        int actualNumberOfToys = (int) (long) emf.createEntityManager().createNativeQuery("SELECT COUNT(id) FROM Toy").getSingleResult();
        assertEquals(expectedNumberOfToys, actualNumberOfToys);
        //assert relationship
        Toy toy = emf.createEntityManager().createQuery("SELECT toy FROM Toy toy WHERE toy.name = 'Gummi bear'",Toy.class).getSingleResult();
        assertTrue(toy.getTools().contains(t));
    }
   // TODO: create test case with cascading updates when related object has changed.

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id) to see that delete are cascaded forth and back between toys and tools");
        Tool t = facade.delete(tool1.getId());
        int expectedNumber = 2; //cascadeType delete on both ends will remove t1 -> then toy 1 and toy 2 -> then from toy2 -> t2
        int actualNumber = facade.getAll().size();
        assertEquals(expectedNumber, actualNumber);
        assertEquals(t, tool1);
        //assert that toys removed
        Toy found = emf.createEntityManager().find(Toy.class, toy1.getId());
        assertTrue(found == null);
    }
}