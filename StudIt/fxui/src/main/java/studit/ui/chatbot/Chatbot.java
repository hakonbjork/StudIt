package studit.ui.chatbot;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import studit.core.chatbot.ChatbotManager;
import studit.core.chatbot.Response;
import studit.core.mainpage.CourseList;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.DirectStuditModelAccess;
import studit.ui.remote.RemoteStuditModelAccess;

public class Chatbot {

  private Stage chatStage;
  private ChatbotManager chatbotManager;
  private ChatbotController controller;

  /**
   * Initialize a new Chatbot instance and display the chatbot to the screen.
   * 
   * @param remoteAccess Direct/Remote access.
   */
  public Chatbot(RemoteStuditModelAccess remoteAccess) {
    displayWindow();
    try {
      CourseList courseList = remoteAccess.getCourseList();
      chatbotManager = new ChatbotManager(courseList.getCourseNameList());
    } catch (ApiCallException e) {
      System.out.println("Error -> Could not establish connection to server");
    }
  }

  /**
   * Intialize a new chatbot instance without UI and with offline remoteAccess ->
   * used for testing purposes only.
   * 
   * @param directAccess dummy value.
   * @throws ApiCallException unknown cause, should never be thrown.
   */
  public Chatbot(boolean directAccess) throws ApiCallException {
    RemoteStuditModelAccess remoteAccess = new DirectStuditModelAccess();
    CourseList courseList = remoteAccess.getCourseList();
    chatbotManager = new ChatbotManager(courseList.getCourseNameList());
  }

  /*
   * Opens a new window for our chatbot
   */
  private void displayWindow() {
    try {
      // Load the FXML
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/studit/ui/Chatbot.fxml"));
      final Parent root = loader.load();

      chatStage = new Stage();
      Scene scene = new Scene(root);

      // Get the active controller and set its stage.
      ChatbotController controller = (ChatbotController) loader.getController();
      controller.setStage(chatStage);

      // Setting the background to be transparent, so we can create rounded corners in
      // our css file
      chatStage.initStyle(StageStyle.TRANSPARENT);
      scene.setFill(Color.TRANSPARENT);

      // Load css.
      scene.getStylesheets().setAll(getClass().getResource("/studit/ui/chatbot.css").toExternalForm());
      chatStage.setScene(scene);
      chatStage.setTitle("Chatbot");
      chatStage.show();

      this.controller = controller;
    } catch (IOException e) {
      System.out.println("Error loading ChatbotController.FXML -> Is the file corrupt?");
    }
  }

  /**
   * If the chatbot is minimized, move it to the front.
   */
  public void show() {
    chatStage.setIconified(false);
  }

  /**
   * Manages the user entered input and executes commands accordingly.
   * 
   * @param input - the user input we want to process
   * 
   * @return chatbot response
   */
  public Response manageInput(String input) {
    return chatbotManager.manageInput(input);
  }

  /**
   * Get the active Chatbot Controller.
   * 
   * @return the controller
   */
  public ChatbotController getController() {
    return controller;
  }

}
