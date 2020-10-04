package studit.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.junit.ExceptionFactory;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.base.WindowMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import studit.core.chatbot.Chatbot;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

public class AppTest extends ApplicationTest {

    private final AppController appController = new AppController();
    private ListView<String> listView = new ListView<>();


    @BeforeEach
    public void setup() throws Exception {
        FxToolkit.setupSceneRoot(() -> {
            listView = new ListView<>();
            listView.setItems(observableArrayList("TDT4109", "TMA4145", "TTM4175", "IT1901"));
            listView.setPlaceholder(new Label("Empty!"));
            return new StackPane(listView);
        });
        FxToolkit.showStage();
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(appController.rootPane.getScene());
        stage.show();
        stage.toFront();
  }

    @Test
    public void hasMainPageButton() {
        BorderPane rootNode = (BorderPane) appController.rootPane.getScene().getRoot();
        Button button = from(rootNode).lookup(".button").query();
        assertEquals("Hjem", button.getText());
    }

    @Test
    public void hasLogoutButton() {
        BorderPane rootNode = (BorderPane) appController.rootPane.getScene().getRoot();
        Button button = from(rootNode).lookup(".button").query();
        assertEquals("Logg ut", button.getText());
    }

    @Test
    public void testLogoutAction() {
        FxAssert.verifyThat(window("Login"), WindowMatchers.isShowing());
    }
    
    @Test
    public void testOpenChatBot() {
        FxAssert.verifyThat(window("Chatbot"), WindowMatchers.isShowing());
    }

    
    @Test 
    public void testClickOnCourse() {
        ListView<String> coursesList = find("#coursesList");
        clickOn(ListCell).type(text);
        
        
    }

    @Test
    public void hasListCell() {
        assertThat(listView).hasListCell("alice");
    }

    @Test
    public void hasExactlyNumItems() {
        assertThat(listView).hasExactlyNumItems(4);
    }

    // @Test 
    // public void testClickOnMainPage() {
    //     Button button = find("#mainPage_btn");
    //     clickOn(button);
    //     verify
        
    // }

//     /** 
//     * Helper function to get a row from a ListView.
//     * Type T is the type of the ListView data model.
//     */
//    fun getListViewRow(viewId: String, row: Int): ListCell<String> { 
//       val listView = lookup(viewId).query<ListView<String>>() 
//       return from(listView).lookup(".list-cell").nth(row).query() 
//    }
//    fun <T> getListViewRowByFirstName(viewId: String, textToFind: String): ListCell<T>? { 
//       val listView = lookup(viewId).query<ListView<T>>() 
//       val cells = from(listView).lookup(".list-cell").queryAll<ListCell<T>>() 
//       // assumes type T has a toString method starting with first name! 
//       return cells.find { it.item.toString().toLowerCase().startsWith(textToFind.toLowerCase()) } 
//    }

    }









