package studit;

import java.util.HashMap;

public class LoginManager{

    private  static HashMap<String, String>  DB = new HashMap<String, String>();

    public LoginManager(){
        initialize();
    }

    ///Setter en foreløpig database med brukernavn og passord
    public static void initialize(){
        DB.put("user", "password");
        DB.put("admin", "password1");
    }

    ///Sjekker om følgende brukernavn og passord eksisterer sammen i databasen
    public static boolean match(String username, String password){
        if (DB.containsKey(username)){
            if (DB.get(username).equals(password)){
                return true;
            }
        }
        return false;
    }



    

    

}