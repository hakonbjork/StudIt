package studit.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import studit.core.chatbot.Chatbot;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;


public class CourseController implements Initializable{
    
    public static Chatbot chatbot = null;
    private String comment;

    @FXML
    private BorderPane rootPane;

    @FXML
    private Label label;

    @FXML
    private Button mainPage_btn;

    @FXML
    private Button chatbot_btn;

    @FXML
    private Button ntnu_btn;

    @FXML
    private Button logout_btn;

    @FXML
     private Button addComment_btn;

    @FXML
    private TextArea commentField;

     


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
               App.primaryStage.setScene(scene);
               App.start();

               } catch (IOException e) {
                    System.out.println(e);
                    
               // //this.rootPane = new FXMLLoader.load(getClass().getResource("App.fxml"));
               // mainPage.getScene().getWindow().hide();

               // // BorderPane newPane = FXMLLoader.load(getClass().getResource("App.fxml"));
               // // rootPane.getChildren().setAll(newPane);

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
     void addComment(ActionEvent event) {
        if(commentField.isEmpty){
             System.out.println("empty field");
        }
        else {
          comment = commentField.getText();
          comment1.setText(comment);
        }
   }
}
