package studit.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import studit.core.mainpage.CourseItem;

public class NyttFagController {


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

    private AppController controller;

    @FXML
    public void initialize() {
        
        System.out.println("init nyttfag controller");

    }

    public void initData(AppController controller){

        this.controller = controller;

    }
        
    @FXML
    void leggTilFag(){

        String fagkode = fagkodeField.getText();
        String fagnavn = fagnavnField.getText();
        String rate = rateField.getText();
        String kommentar = kommentarField.getText();

        CourseItem item = controller.getCourseList().createCourseItem();
        item.setFagkode(fagkode);
        item.setFagnavn(fagnavn);
        item.setScore(rate);
        item.setKommentar(kommentar);

        controller.getCourseList().addCourseItem(item);

        fagkodeField.clear();
        fagnavnField.clear();
        rateField.clear();
        kommentarField.clear();

        controller.updateData();
    }

}
