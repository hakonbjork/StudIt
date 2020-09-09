package studit.core;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Chatbot {
	
	private Stage chatStage;
	
	public Chatbot() {
		displayWindow();
	}
	
	/*
	 * Opens a new window for our chatbot
	 */
	private void displayWindow() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/studit/ui/Chatbot.fxml"));
			chatStage = new Stage();
			Scene scene = new Scene(root);
			
			// Setting the background to be transparent, so we can create rouned corners in our css file
			chatStage.initStyle(StageStyle.TRANSPARENT);
			scene.setFill(Color.TRANSPARENT);
			
			scene.getStylesheets().setAll(getClass().getResource("/studit/ui/chatbot.css").toExternalForm());
			chatStage.setScene(scene);
			chatStage.show();
		} catch (IOException e) {
			System.out.println("Error loading ChatbotController.FXML -> Is the file corrupt?");
			e.printStackTrace();
		}
	}

	public void show() {
		chatStage.setIconified(false);
	}

}
