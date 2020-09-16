package studit.core;

import java.util.HashMap;

public class LoginManager{

    private  static HashMap<String, String>  DB = new HashMap<String, String>();

    public LoginManager(){
        initialize();
    }

    /*
    * Sets a database with username and passwords
    */
    public static void initialize(){
        DB.put("user", "password");
        DB.put("admin", "password1");
    }

    /*
     * Checks if the username and password exists together in the database
     * @param username - The username to be checked
     * @param password - The password to be checked
     * @return true if the username matches the password in the database, else false
     */
    public static boolean match(String username, String password){
        if (DB.containsKey(username)){
            if (DB.get(username).equals(password)){
                return true;
            }
        }
        return false;
    }



    

    

}