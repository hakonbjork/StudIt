package studit.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserManager {

  public static void startStuff() {
    User user = new User("John Appleseed", "user", "user@studit.com", "password");
    User admin = new User("Mark Brownie", "admin", "admin@studit.com", "password1");
    addUser(user);
    addUser(admin);
  }

  /**
   * Adds a user to the database.
   * @return true if successfully adds user, returns false if username is taken
   */
  public static boolean addUser(User user) {
    if (!containsUser(user.getUsername())) {
      ArrayList<User> users = getUsersFromDB();
      users.add(user);
      addUsersToDB(users);

      return true;
    }
    return false;
  }

  /**
   * Removes the user from the databse. No use in app yet, but for testing and
   * future use.
   * @param user - The user to be removed
   */
  public static void removeUser(User user) {
    if (containsUser(user.getUsername())) {
      ArrayList<User> users = getUsersFromDB();
      users.remove(user);
      addUsersToDB(users);
    }
    System.out.println("Error: User not in databse");
  }

  /**
   * Checks if the user exist in the database, to login.
   * 
   * @param username Username of the user to be checked
   * @param password Password of the username to be checked
   * @return true if the user exists
   */
  public static boolean containsUser(String username, String password) {
    ArrayList<User> users = getUsersFromDB();
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        if (user.getPassword().equals(password)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Checks if the user exist in the database, to login.
   * 
   * @param username Username of the user to be checked
   * @return true if the user exists
   */
  public static boolean containsUser(String username) {
    ArrayList<User> users = getUsersFromDB();
    if (users == null) {
      return false;
    }
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Saves the users to json file.
   * 
   * @param users - The users to be saved to the database (json file)
   */
  private static void addUsersToDB(List<User> users) {

    ObjectMapper mapper = new ObjectMapper();
    try {
      // Note: When running localy, we need start the path with "Studit/src/..."
      // But, when using mvn javafx:run from cd Studit, the path should start with
      // "src/..."
      mapper.writeValue(Paths.get("../core/src/main/resources/studit/db/userDB.json").toFile(), users);

    } catch (IOException e) {
      System.out.println("Error occured while saving users to json file");
      e.printStackTrace();
    }

  }

  /**
   * Loads the users from database (the json file).
   * 
   * @return The List of users that is fetched from the file
   */
  private static ArrayList<User> getUsersFromDB() {

    ObjectMapper mapper = new ObjectMapper();

    try {
      List<User> usersList = Arrays
          .asList(mapper.readValue(Paths.get("../core/src/main/resources/studit/db/userDB.json").toFile(), User[].class));

      return new ArrayList<User>(usersList);

    } catch (IOException e) {
      System.out.println("Error occured while fetching users from json file");
      e.printStackTrace();
      return null;
    }

  }

}
