package utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMF_Creator {

    /**
     * Call this method before all integration tests that uses the Grizzly
     * Server and the Test Database (in @BeforeAll ) Remember to call
     * enRestTestWithDB() (in @AfterAll)
     */
    public static void startREST_TestWithDB() {
        System.setProperty("IS_INTEGRATION_TEST_WITH_DB", "testing");
    }

    /*
      Call this method in your @AfterAll method if startREST_TestWithDB() was previously called
     */
    public static void endREST_TestWithDB() {
        System.clearProperty("IS_INTEGRATION_TEST_WITH_DB");
    }

    public static EntityManagerFactory createEntityManagerFactory() {
        return createEntityManagerFactory(false);
    }

    public static EntityManagerFactory createEntityManagerFactoryForTest() {
        return createEntityManagerFactory(true);
    }

    private static EntityManagerFactory createEntityManagerFactory(boolean isTest) {

        //Check if we are in production and use credentials passed in on the server via CATALINA_OPTS        
        if (System.getenv("CATALINA_OPTS") != null) {
            String user = System.getProperty("USER");
            String pw = System.getProperty("PW");
            String connection_str = System.getProperty("CONNECTION_STR");
            if(user == null || pw == null || connection_str==null){
                throw new RuntimeException("one or all of the values used to setup the database is missing");
            }
            Properties props = new Properties();
            props.setProperty("javax.persistence.jdbc.user", user);
            props.setProperty("javax.persistence.jdbc.password", pw);
            props.setProperty("javax.persistence.jdbc.url", connection_str);
            return Persistence.createEntityManagerFactory("pu", props);

        }

        String puName = isTest || System.getProperty("IS_INTEGRATION_TEST_WITH_DB") != null ? "puTest" : "pu"; //Only legal name
        if (puName.equals("puTest")) {
            System.out.println("Using the test database via puTest ");
        } else {

            System.out.println("Using the dev database via pu ");
        }
        return Persistence.createEntityManagerFactory(puName, null);
    }
}
