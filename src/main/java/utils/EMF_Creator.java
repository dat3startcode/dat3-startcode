package utils;

import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EMF_Creator {

    public enum Strategy {
        NONE {
            @Override
            public String toString() {
                return "drop-and-create";
            }
        },
        CREATE {
            @Override
            public String toString() {
                return "create";
            }
        },
        DROP_AND_CREATE {
            @Override
            public String toString() {
                return "drop-and-create";
            }
        }
    };

    
    public static EntityManagerFactory createEntityManagerFactory(
            String puName,
            String connection_str,
            String user,
            String pw,
            Strategy strategy) {

        Properties props = new Properties();
        
        //A test running on a different thread can alter values to use via these properties
        System.out.println("IS Testing: " + System.getProperty("IS_TEST"));
        if (System.getProperty("IS_TEST") != null) {
            connection_str = System.getProperty("IS_TEST");
            user = System.getProperty("USER") != null ? System.getProperty("USER") : user;
            pw = System.getProperty("PW") != null ? System.getProperty("PW") : pw;
        }
        
        //A deployment server MUST set the following values which will override the defaults
        boolean isDeployed = (System.getenv("DEPLOYED") != null);
        if (isDeployed) {
            user = System.getenv("USER");
            pw = System.getenv("PW");
            connection_str = System.getenv("CONNECTION_STR");
        }
        /*
        On your server in /opt/tomcat/bin/setenv.sh   add the following WITH YOUR OWN VALUES
        
        export DEPLOYED="DEV_ON_DIGITAL_OCEAN"
        export USER="dev"
        export PW="ax2"
        export CONNECTION_STR="jdbc:mysql://localhost:3306/mydb"
        
        Then save the file, and restart tomcat: sudo systemctl restart tomcat
        */
        
        System.out.println("USER ------------> "+user);
        System.out.println("PW --------------> "+pw);
        System.out.println("CONNECTION STR---> "+connection_str);
        System.out.println("PU-Strategy---> "+strategy.toString());
        
        props.setProperty("javax.persistence.jdbc.user", user);
        props.setProperty("javax.persistence.jdbc.password", pw);
        props.setProperty("javax.persistence.jdbc.url", connection_str);
        if (strategy != Strategy.NONE) {
            props.setProperty("javax.persistence.schema-generation.database.action", strategy.toString());
        }
        return Persistence.createEntityManagerFactory(puName, props);
    }
}