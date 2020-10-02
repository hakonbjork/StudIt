package studit.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.core.chatbot.Chatbot;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.json.CoursePersistence;

public class AppController { 

    @FXML private ListView<String> coursesList;
    @FXML private Button mainPageAction;
    @FXML private Button openChatBot;
    @FXML private Button ntnuAction;
    @FXML private Button logoutAction;
    @FXML private BorderPane rootPane;
    @FXML private AnchorPane mainPane;

    @FXML private Button mainPage_btn;
    @FXML private Button chatbot_btn;
    @FXML private Button ntnu_btn;
    @FXML private Button logout_btn;

    public static Chatbot chatbot = null;

    private CourseList courseList = new CourseList();

    private CoursePersistence coursePersistence = new CoursePersistence();

    // makes class more testable
    CourseList getCourseList() {
        return this.courseList;
    }

    ObservableList<String> list = FXCollections.observableArrayList();

    private void initializeCourseList() {
        // setter opp data
        Reader reader = null;
        // try to read file from home folder first

        try {
            reader = new FileReader("src/main/resources/studit/db/db.json");
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    
        try {

            courseList = coursePersistence.readCourseList(reader);

            for (CourseItem item: courseList.getCourseItems()){

                list.add(item.getFagkode());

            }

        coursesList.setItems(list);
            

        } catch (IOException e) {
      
            System.out.println(e);

        } finally {

        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            // ignore
        }
    }
  }


    @FXML
    public void initialize() {
        
        initializeCourseList();

        updateData();

        coursesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Actions on clicked list item
        mouseClicked();
    }
    
   
    /**
    * Gives the user a choice to open NTNU homepage in web-browser
    * @return none
    */
    @FXML
    void openChatBot(ActionEvent event) {
        if (chatbot == null) {
    		chatbot = new Chatbot();
    	} else {
    		chatbot.show();
    	}
    }

    public static void closeChatbot() {
        chatbot = null;
    }



    /**
    * Function to search for subjects. The listview will then only show subjects with 
    * the letters in the search field
    * @return none
    */
    @FXML
    void handleSearchAction(KeyEvent event) {
        

    }

    public void changed(ObservableValue observable, Object oldVal, Object newVal) {
        search((String) oldVal, (String) newVal);
    }

    public void search(String oldVal, String newVal) {
        if (oldVal != null && (newVal.length() < oldVal.length())) {
        coursesList.setItems(list);
    }

        String value = newVal.toUpperCase();

        ObservableList<String> subentries = FXCollections.observableArrayList();
        for (Object entry : coursesList.getItems()) {
            boolean match = true;
            String entryText = (String) entry;
        if (!entryText.toUpperCase().contains(value)) {
            match = false;
            break;
        } if (match) {
            subentries.add(entryText);
        }
    }
        coursesList.setItems(subentries);
}



    /**
    * Should give the option to go to the subjects web-page
    * @return none
    */
    @FXML
    void handleNtnuAction(ActionEvent event) {

        //go to NTNU homepage (question if you want to open web-browser)?
        //or a new window with information about NTNU?
    
    }
        
    
    
    @FXML
    void searchView(ActionEvent event) {

    }

     
     /** 
     * logs user out, and opens to login scene, closes current scene
     */
     @FXML void handleLogoutAction(ActionEvent event) {
         try {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
    
        Stage stage2 = new Stage();
        stage2.setScene(new Scene(root));
        stage2.setTitle("StudIt");
        stage2.show();

        Stage stage = (Stage) logout_btn.getScene().getWindow();
            stage.hide();
            

        } catch (IOException e) {
                System.out.println(e);
            }
	    }


    /** 
    * redirects user to the main page
    * @return none
    */
    @FXML
    void handleMainPageAction() {
        //nothing should really happen when you are in the home page other than maybe refresh(?)
    }


    
    /** A function that does something when a element in the listview is clicked on.
    * @return None
    */
    public void mouseClicked(){
		//Detecting mouse clicked
		coursesList.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				String name = coursesList.getSelectionModel().getSelectedItem();
                    System.out.println(name);	
                    		
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Course.fxml"));
                        Parent root = loader.load();
   
                    
                        CourseController courseController = loader.getController();
                        courseController.setLabel(name);
   
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
   
                        } catch (IOException e) {
                            System.out.println(e);
                        }
			}
		});
	}


   

    /** This function should actually fetch data from a database. This will be implemented later.
    * @return None
    */
    public void updateData() {

        for(CourseItem item: courseList){

            if(!list.contains(item.getFagkode())){
                list.add(item.getFagkode());
            }

        }
        coursesList.setItems(list);

        try (Writer writer = new FileWriter("src/main/resources/studit/db/db.json", StandardCharsets.UTF_8)) {

        coursePersistence.writeCourseList(courseList, writer);

      } catch (IOException e) {

        System.err.println("Fikk ikke skrevet til db.json");
      }


        
    }
}