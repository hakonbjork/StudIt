// package studit.ui;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Label;
// import javafx.scene.control.TextArea;
// import javafx.scene.layout.BorderPane;
// import javafx.scene.layout.FlowPane;
// import studit.core.chatbot.Chatbot;
// import javafx.scene.Parent;
// import javafx.scene.control.Button;
// import javafx.fxml.FXMLLoader;
// import javafx.fxml.Initializable;
// import javafx.stage.Stage;


package studit.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;


public class CourseController implements Initializable { 

    CourseController courseController = new CourseController();


    public static Chatbot chatbot = null;

    @FXML private BorderPane rootPane;
    @FXML private FlowPane flowPane;

    @FXML private Button mainPageAction;
    @FXML private Button chatbotAction;
    @FXML private Button ntnuAction;
    @FXML private Button logoutAction;
    @FXML private Button addComment;
    @FXML private Button mainPage_btn;
    @FXML private Button openChatBot;


    @FXML private Label label1;
    @FXML private Label label;

    @FXML private TextArea comment1;
    @FXML private TextArea comment2;
    @FXML private TextArea comment3;
    @FXML private TextArea commentField;

   
     public void initialize(String name) {
		label.setText(name);
     }
     
    /*
    * logs user out, and goes to login scene
     */
    @FXML
    void logoutAction() {
          Parent root2 = new FXMLLoader.load(getClass().getResource("login.fxml"));
          Scene scene = new Scene(root2);
          App.primaryStage.setScene(scene);
          

     } catch (IOException e) {
          System.out.println(e);
    }

    /*
    * Goes back to main page
    */
    @FXML
    void mainPageAction() {
          try {
               Parent root2 = new FXMLLoader.load(getClass().getResource("App.fxml"));
               Scene scene = new Scene(root2);
               CourseController.primaryStage.setScene(scene);
               App.start();

          } catch (IOException e) {
               System.out.println(e);
               }
          }

    /*
    * Gives the user a choice to open NTNU homepage in web-browser
    */
    @FXML
    void ntnuAction(ActionEvent event) {
          System.out.println("NTNU");
    }


    @FXML
    void openChatBot(ActionEvent event) {
         if (chatbot == null) {
    		chatbot = new Chatbot();
    	} else {
    		chatbot.show();
    	}
    }

    public void setLabel(String text){
       label.setText(text);
   }

     @FXML
     void addCommentAction(ActionEvent event) {

          String comment = commentField.getText();
          comment1.setText(comment);
   }
}
