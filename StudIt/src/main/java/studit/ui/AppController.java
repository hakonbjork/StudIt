package studit.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;


public class AppController{ 
    
    ObservableList<String> list = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        loadData();
    }

    @FXML
    private ListView<String> coursesList;

     @FXML
    private TextField search_field;

    @FXML
    private Button chatbot_btn;

    @FXML
    void openChatBot(ActionEvent event) {

    }

    //Ved å skrive inn noe i søkefeltet skal du få opp en listview
    @FXML
    void searchView(ActionEvent event) {

    }

    //Denne funksjonen burde egentlig være en som henter noe fra en database der data om hvert fag blir lagret.
    //Så det under blir mockdata.
    private void loadData() {
    String a = "TDT4109";
    String b =  "TMA4145";
    String c = "TTM4175";
    String d = "IT1901";
    list.addAll(a,b,c,d);
    coursesList.getItems().addAll(list);
    }

}
