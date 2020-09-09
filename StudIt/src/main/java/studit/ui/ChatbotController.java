package studit.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatbotController implements Initializable {
	
	private Stage stage = null;
	private double xOffset = 0, yOffset = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
    @FXML
    private BorderPane pane_chatbot;

    @FXML
    private Button btn_minimize;

    @FXML
    private Button btn_exit;

    @FXML
    void exitChatbot(ActionEvent event) {
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
    void setOffset(MouseEvent event) {
    	// If we have not yet loaded the current stage, load it from the event
    	if (stage == null) {
    		Node source = (Node) event.getSource();
    		stage = (Stage) source.getScene().getWindow();
    	}
    	xOffset = event.getSceneX();
        yOffset = event.getSceneY();
    }

}
