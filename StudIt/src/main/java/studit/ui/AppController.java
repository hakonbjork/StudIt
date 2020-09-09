package studit.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import studit.core.Chatbot;

public class AppController implements Initializable {
	
	public static Chatbot chatbot = null;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	@FXML
    private Button btn_chat;
	
	
	/*
	 * Opens our new ChatBot window
	 */
    @FXML
    private void chatClick(ActionEvent event) {
    	if (chatbot == null) {
    		chatbot = new Chatbot();
    	} else {
    		chatbot.show();
    	}
    }
    
    public static void closeChatbot() {
    	chatbot = null;
    }

    
	

}
