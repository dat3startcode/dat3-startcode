package utils;


import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Settings;

public class SettingsTest {
    
    @Test
    public void testGetSingleProperty(){
        assertEquals("6666", Settings.getPropertyValue("db.port"));
    }
    
    @Test
    public void getDEV_DBConnection(){
        assertEquals("jdbc:mysql://localhost:6666/nameOfDb", Settings.getDEV_DBConnection());
    }
    
    @Test
    public void getTEST_DBConnection(){
        assertEquals("jdbc:mysql://localhost:6666/nameOfTestDB", Settings.getTEST_DBConnection());
    }
    
}
