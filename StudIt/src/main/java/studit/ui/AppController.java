package studit.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import studit.core.Chatbot;

public class AppController implements Initializable {

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
    private Button btn_chat;
	
	
	/*
	 * Opens our new ChatBot window
	 */
    @FXML
    void chatClick(ActionEvent event) {
    	new Chatbot();
    }
	

}
