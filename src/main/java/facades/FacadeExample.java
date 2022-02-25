package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ChuckDTO;
import dtos.RenameMeDTO;
import entities.RenameMe;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

//import errorhandling.RenameMeNotFoundException;
import utils.EMF_Creator;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class FacadeExample {

    private static final String CHUCK_URL = "https://api.chucknorris.io/jokes/random";
    private static final String DAD_URL = "https://icanhazdadjoke.com";
    private static FacadeExample instance;
    private static EntityManagerFactory emf;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    //Private Constructor to ensure Singleton
    private FacadeExample() {}
    
    
    /**
     * 
     * @param _emf
     * @return an instance of this facade class.
     */
    public static FacadeExample getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FacadeExample();
        }
        return instance;
    }


    public String readJoke(String uRL_STRING){
        String jsonStr = "";
        try {
            URL url = new URL(uRL_STRING);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Accept", "application/json");
            con.setRequestProperty("User-Agent", "MyApp");
            try (Scanner scan = new Scanner(con.getInputStream())) {
                while (scan.hasNext()) {
                    jsonStr += scan.nextLine();
                }
            }
        } catch (IOException ioex) {
            System.out.println("Error: " + ioex.getMessage());
        }
        return jsonStr;
    }
    public ChuckDTO getChuckJoke(){
        ChuckDTO chuck  = gson.fromJson(readJoke(CHUCK_URL), ChuckDTO.class);
        return chuck;
    }

    public static void main(String[] args) {
        FacadeExample facade = getFacadeExample(EMF_Creator.createEntityManagerFactory());

        String output = facade.readJoke(DAD_URL);
        System.out.println(output);
        ChuckDTO chuck = facade.getChuckJoke();
        System.out.println("DTO:");
        System.out.println(chuck.getValue());
    }
}
