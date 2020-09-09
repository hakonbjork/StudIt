package studit.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.core.Chatbot;

public class ChatbotController implements Initializable {
	
	private Stage stage = null;
	private double xOffset = 0, yOffset = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	//--------------------------------------------------Member Initialization-----------------------------------------------
	 
    @FXML
    private BorderPane pane_chatbot;

    @FXML
    private Button btn_minimize;

    @FXML
    private Button btn_exit;
    
    @FXML
    private TextField txt_user_entry;
    
	//------------------------------------------------------Widget Logic----------------------------------------------------

    @FXML
    void exitChatbot(ActionEvent event) {
    	AppController.closeChatbot();
    	final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        
    }

    @FXML
    void minimizeChatbot(ActionEvent event) {
    	final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
    	stage.setIconified(true);
    }
    
    /*
     * Moves our chatbot window when dragged by the toolbar
     */
    @FXML
    void moveWindow(MouseEvent event) {
    	if (stage != null) {
    		 stage.setX(event.getScreenX() - xOffset);
             stage.setY(event.getScreenY() - yOffset);
    	}
    }

    /*
     * Sets new cursor location (x & yOffset) so that we can drag our application by the toolbar
     */
    @FXML
    private void setOffset(MouseEvent event) {
    	// If we have not yet loaded the current stage, load it from the event
    	if (stage == null) {
    		Node source = (Node) event.getSource();
    		stage = (Stage) source.getScene().getWindow();
    	}
    	xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }
    
    /*
     * When user presses enter key, send the input to the Chatbot to perform an action accordingly
     */
    @FXML
    private void userEntry(KeyEvent event) {
    	if (event.getCode() == KeyCode.ENTER) {
    		String userInput = txt_user_entry.getText();
    		txt_user_entry.setText("");
    		AppController.chatbot.manageInput(userInput);
    	}
    }

}
