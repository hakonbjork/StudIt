package studit.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Scanner;

public class LoginManager {

    private static HashMap<String, String> DB = new HashMap<String, String>();

    public LoginManager() {
        initialize();
    }

    /*
     * Sets a database with username and passwords
     */
    public static void initialize() {
        DB.put("user", "password");
        DB.put("admin", "password1");
    }

    /*
     * Checks if the username and password exists together in the database
     * 
     * @param username - The username to be checked
     * 
     * @param password - The password to be checked
     * 
     * @return true if the username matches the password in the database, else false
     */
    public static boolean match(String username, String password) {
        return UserManager.containsUser(username, password);
    }
}
