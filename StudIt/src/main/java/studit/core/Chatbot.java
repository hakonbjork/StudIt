package studit.core;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Chatbot {
	
	public Chatbot() {
		displayWindow();
	}
	
	private void displayWindow() {
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("/studit/ui/Chatbot.fxml"));
			Stage chatStage = new Stage();
			
			chatStage.setTitle("Chat");
			chatStage.initStyle(StageStyle.TRANSPARENT);
			
			Scene scene = new Scene(root);
			scene.setFill(Color.TRANSPARENT);
			scene.getStylesheets().setAll(getClass().getResource("/studit/ui/chatbot.css").toExternalForm());
			chatStage.setScene(scene);
			chatStage.show();
		} catch (IOException e) {
			System.out.println("Error loading ChatbotController.FXML -> Is the file corrupt?");
			e.printStackTrace();
		}
	}

}
