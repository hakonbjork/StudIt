package studit.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.core.chatbot.Chatbot;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.json.CoursePersistence;

public class AppController {

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

        coursesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //Actions on clicked list item
        mouseClicked();
    }

    @FXML 
    private Button nyttfagButton;

    @FXML
    private ListView<String> coursesList;

     @FXML
    private TextField search_field;

    @FXML
    private Button chatbot_btn;

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

    @FXML
    void nyttFagButtonClickedOn(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("NyttFag.fxml"));

        Stage stage = new Stage();
            stage.setScene(
            new Scene(loader.load())
        );
    

        NyttFagController controller = loader.getController();

            controller.initData(this);

       stage.show();
    
    }
    
    @FXML
    void searchView(ActionEvent event) {

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
                        courseController.setLabelText(name);
   
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
    public void loadData() {


    }

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