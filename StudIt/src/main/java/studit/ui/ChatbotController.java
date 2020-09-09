package studit.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatbotController implements Initializable {
	
	private Stage stage = null;
	private double xOffset = 0, yOffset = 0;
	private int lineBreakLength = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		txt_user_entry.textProperty().addListener(l -> checkForLineBreak());
	}
	
	private void checkForLineBreak() {
		if (lineBreakLength == 0) {
			// 8.2 is an arbitrary value based on font and font size, hard to make less hardcoded
			lineBreakLength = (int) (txt_user_entry.getWidth() / 8.1);
		}	
		
		if (txt_user_entry.getText().length() % lineBreakLength == 0) {
			txt_user_entry.setText(txt_user_entry.getText() + "\n");
		}

	}

	//--------------------------------------------------Member Initialization-----------------------------------------------
	 
    @FXML
    private BorderPane pane_chatbot;

    @FXML
    private Button btn_minimize;

    @FXML
    private Button btn_exit;
    
    @FXML
    private TextArea txt_user_entry;
    
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
    		// Make sure that the caret is at first position for a new command!
    		txt_user_entry.selectPositionCaret(0);
    		AppController.chatbot.manageInput(userInput);
    	}
    }

}
