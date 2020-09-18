package studit.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import studit.core.mainpage.CourseListManager;

public class NyttFagController {

    private CourseListManager courseListManager = new CourseListManager();

    @FXML
    private TextField fagkodeField;

    @FXML
    private TextField fagnavnField;

    @FXML
    private TextField rateField;

    @FXML
    private TextField kommentarField;

    @FXML
    private Button leggTilButton;

    @FXML
    public void initialize() {

        System.out.println("init nyttfag controller");

    }
        
    @FXML
    void leggTilFag(){

        String fagkode = fagkodeField.getText();
        String fagnavn = fagnavnField.getText();
        String rate = rateField.getText();
        String kommentar = kommentarField.getText();

        courseListManager.writeToDb(fagkode, fagnavn, rate, kommentar);

        fagkodeField.clear();
        fagnavnField.clear();
        rateField.clear();
        kommentarField.clear();

    }

}
