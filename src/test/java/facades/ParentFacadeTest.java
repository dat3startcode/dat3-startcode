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

class ParentFacadeTest {

    private static EntityManagerFactory emf;
    private static IDataFacade<Parent> facade;
    Parent p1,p2;
    Child c1,c2;
    IdentificationCard i1;

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
            c1 = new Child("Dorthea", 3);
            c2 = new Child("Frederik", 6);
            i1 = new IdentificationCard(IdentificationCard.IdentificationType.DRIVERS_LICENS,"3333333333","Should be renewed soon");

            p1.addChild(c1);
            p1.addCard(i1);
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
//        Remove any data after each test was run
//        emf.close();
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
    void createPersistCard() {
        System.out.println("Testing create(Parent p) see if IdentificationCard is persisted to");
        Parent p = new Parent("TestParent",10);
        IdentificationCard i = new IdentificationCard(IdentificationCard.IdentificationType.DRIVERS_LICENS, "44444444444","Issued in 75");
        p.addCard(i);
        Parent created = facade.create(p);
        assertTrue(created.getCards().contains(i));
        IdentificationCard ic = emf.createEntityManager().createQuery("SELECT i FROM IdentificationCard i WHERE i.number = '44444444444'",IdentificationCard.class).getSingleResult();
        Parent expected = created;
        Parent actual = ic.getParent();
        assertEquals(expected,actual);
    }

    @Test
    void getById() throws EntityNotFoundException {
        System.out.println("Testing getbyid(id)");
        Parent expected = p1;
        Parent actual = facade.getById(p1.getId());
        assertEquals(expected, actual);
        //assert child
        assert(actual.getChildren().contains(c1));
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
        //assert no duplicates
        int expectedNumberOfChildren = 2;
        int actualNumberOfChildren = (int)(long) emf.createEntityManager().createNativeQuery("SELECT COUNT(id) FROM CHILD").getSingleResult();
        assertEquals(expectedNumberOfChildren, actualNumberOfChildren);
        //assert bidirectional relationship
        Parent expectedParent = p;
        Parent actualParent = emf.createEntityManager().find(Child.class, c2.getId()).getParent();
        assertEquals(expectedParent, actualParent);
    }

    @Test
    void updateWithNewChildren() throws EntityNotFoundException {
        System.out.println("Testing Update(Parent p) with known children");
        p2.addChild(new Child("Hobs",12));
        p2.addChild(new Child("Josephine",2));
        Parent p = facade.update(p2);
        int expected = 2;
        int actual = p.getChildren().size();
        assertEquals(expected,actual);
        int expectedNumberOfChildren = 4;
        int actualNumberOfChildren = (int)(long)emf.createEntityManager().createNativeQuery("SELECT COUNT(id) FROM CHILD").getSingleResult();
        assertEquals(expectedNumberOfChildren, actualNumberOfChildren);
    }

    @Test
    void delete() throws EntityNotFoundException {
        System.out.println("Testing delete(id)");
        Parent p = facade.delete(p1.getId());
        int expectedNumber = 1;
        int actualNumber = facade.getAll().size();
        assertEquals(expectedNumber, actualNumber);
        assertEquals(p,p1);
        //assert children removed
        Child found = emf.createEntityManager().find(Child.class, c1.getId());
        assertTrue(found==null);
    }

    @Test
    void deleteWithoutCascade() throws EntityNotFoundException {
        System.out.println("Testing deleteWithoutCascade(id) so the child of the parent are still there when parent is deleted");
        Parent p = new ParentFacade().deleteWithoutCascading(p1.getId());
        int expectedNumber = 1;
        int actualNumber = facade.getAll().size();
        assertEquals(expectedNumber, actualNumber);
        assertEquals(p,p1);
        //assert children detached
        Child found = emf.createEntityManager().find(Child.class, c1.getId());
        assertTrue(found.getParent()==null);
    }

    @Test
    void deleteAndRemoveCard() throws EntityNotFoundException {
        System.out.println("Testing delete to see that the cards are remove to");
        facade.delete(p1.getId());
        assertNull(emf.createEntityManager().find(IdentificationCard.class,i1.getId()));
    }
}