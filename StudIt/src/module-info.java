module studit {
	requires javafx.fxml;
	requires transitive javafx.graphics;
	requires javafx.controls;
	
	exports studit;

	opens studit to javafx.fxml;
}