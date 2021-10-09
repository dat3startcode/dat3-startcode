package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ChildDTO;
import dtos.ParentDTO;
import entities.Child;
import entities.Parent;
import entities.Toy;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseOptions;
import io.restassured.response.ValidatableResponse;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.BeforeAll;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import entities.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled
public class ParentResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Parent p1, p2;
    private static Child c1, c2;
    private static Toy t1, t2;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Parent("Henrik",76);
        p2 = new Parent("Betty",76);
        c1 = new Child("Joseph",45);
        c2 = new Child("Alberta",43);
        t1 = new Toy("Pulling rope",3);
        t2 = new Toy("Skateboard", 6);

        p1.addChild(c1);
        p1.addChild(c2);
        c1.addToy(t1);
        c2.addToy(t2);

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Parent.deleteAllRows").executeUpdate();
            em.persist(t1);
            em.persist(t2);
            em.persist(c1);
            em.persist(c2);
            em.persist(p1);
            em.persist(p2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/parent").then().statusCode(200);
    }

    @Test
    public void testGetById()  {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/parent/{id}",p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(p1.getId()))
                .body("name", equalTo(p1.getName()))
                .body("children", hasItems(hasEntry("name","Joseph"),hasEntry("name","Alberta")));
    }

    @Test
    public void testError() {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id", p1.getId()).when()
                .get("/parent/{id}",999999999)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", equalTo(404))
                .body("message", equalTo("The Parent entity with ID: 999999999 Was not found"));
    }

    @Test
    public void testPrintResponse(){
        Response response = given().when().get("/parent/"+p1.getId());
        ResponseBody body = response.getBody();
        System.out.println(body.prettyPrint());

        response
                .then()
                .assertThat()
                .body("name",equalTo("Henrik"));
    }

    @Test 
    public void exampleJsonPathTest() {
        Response res = given().get("/parent/"+p1.getId());
        assertEquals(200, res.getStatusCode());
        String json = res.asString();
        JsonPath jsonPath = new JsonPath(json);
        assertEquals("Henrik", jsonPath.get("name"));
    }

    @Test
    public void getAllParents() throws Exception {
        List<ParentDTO> parentDTOs;

        parentDTOs = given()
                .contentType("application/json")
                .when()
                .get("/parent")
                .then()
                .extract().body().jsonPath().getList("", ParentDTO.class);

        ParentDTO p1DTO = new ParentDTO(p1);
        ParentDTO p2DTO = new ParentDTO(p2);
        assertThat(parentDTOs, containsInAnyOrder(p1DTO, p2DTO));

    }


    @Test
    public void postTest() {
        Parent p = new Parent("Helge",45);
        p.addChild(new Child("Josephine",3));
        ParentDTO pdto = new ParentDTO(p);
        String requestBody = GSON.toJson(pdto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/parent")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Helge"))
                .body("children", hasItems(hasEntry("name","Josephine")));
    }

    @Test
    public void updateTest() {
        p2.addChild(c2);
        p2.setAge(23);
        ParentDTO pdto = new ParentDTO(p2);
        String requestBody = GSON.toJson(pdto);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .put("/parent/"+p2.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", equalTo(p2.getId()))
                .body("name", equalTo("Betty"))
                .body("age", equalTo(23))
                .body("children", hasItems(hasEntry("name","Alberta")));
    }

    @Test
    public void testDeleteParent() {
        given()
                .contentType(ContentType.JSON)
                .pathParam("id", p2.getId())
                .delete("/parent/{id}")
                .then()
                .statusCode(200)
                .body("id",equalTo(p2.getId()));
    }
}
