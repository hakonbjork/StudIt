package studit.ui;


import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import studit.core.chatbot.Chatbot;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.ui.remote.ApiCallException;
import studit.core.users.User;
import studit.ui.remote.RemoteStuditModelAccess;

public class AppController {

  private RemoteStuditModelAccess remoteStuditModelAccess = new RemoteStuditModelAccess();

  private User user = new User("Ida Idasen", "IdaErBest", "IdaElskerHunder@flyskflysk.com",
      "0f0b30a66731e73240b9e331116b57de84f715ab2aea0389bb68129fcf099da3", 1);

  @FXML
    private BorderPane rootPane;

    @FXML
    private TextField searchField;

    @FXML
    private ListView<String> coursesList;

    @FXML
    private Button chatbot_btn;

    @FXML
    private Button logoutAction;

  static Chatbot chatbot = null;
  private ObservableList<String> list = FXCollections.observableArrayList();
  private List<CourseItem> courseList;
  private String label;

  public void setLabel(String label) {
    this.label = label;
  }

  public String getLabel() {
    return this.label;
  }

  public static Chatbot getChatbot() {
    return chatbot;
  }

  public static void newChatbot() {
    chatbot = new Chatbot();
  }

  public void addUser(User user) {
    this.user = user;
  }

  /**
   * Function to initialize AppController.
   * 
   * @throws ApiCallException
   */
  public void initialize() throws ApiCallException {
    loadData();
    coursesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    // Actions on clicked list item
    mouseClicked();
  }

  /**
   * Function to search for subjects. The listview will then only show subjects
   * with the letters in the search field.
   */
  @FXML
  public void handleSearchFieldAction() {
    // Wrap the ObservableList in a FilteredList (initially display all data).
    FilteredList<String> filteredData = new FilteredList<>(this.getData(), (p -> true));

    // Set the filter Predicate whenever the filter changes.
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(courseItem -> {
        // If filter text is empty, display all persons.
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }
        // Compare course name and course code of every CourseItem with the filter text.
        String lowerCaseFilter = newValue.toLowerCase();

        if (courseItem.toLowerCase().contains(lowerCaseFilter)) {
          return true; // filter matches course name

          // } else if (courseItem.toLowerCase().contains(lowerCaseFilter)) {
          // return true; // filter matches course code

        }
        return false; // Does not match
      });
    });

    SortedList<String> sortedData = new SortedList<>(filteredData);
    coursesList.setItems(sortedData);
  }
  

  /**
   * Opens chatbot.
   */
  @FXML
  void openChatBot(ActionEvent event) {
    if (chatbot == null) {
      chatbot = new Chatbot();
    } else {
      chatbot.show();
    }
  }

  /**
   * closes chatbot.
   */
  public static void closeChatbot() {
    chatbot = null;
  }

  /**
   * logs user out, and opens to login scene, closes current scene.
   */
  @FXML
  void handleLogoutAction(ActionEvent event) {
    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
      Parent root = loader.load();

      Stage stage2 = new Stage();
      stage2.setScene(new Scene(root));
      stage2.setTitle("Login");
      stage2.show();

      Stage stage = (Stage) rootPane.getScene().getWindow();
      stage.hide();

    } catch (IOException e) {
      System.out.println(e);
    }
  }


  /**
   * A function that does something when an element in the listview is clicked on.
   */
  public void mouseClicked() {
    // Detecting mouse clicked
    coursesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
      // private String label;
      @Override
      public void handle(MouseEvent arg0) {
        // System.out.println((coursesList.getSelectionModel().getSelectedItem()));
        setLabel(coursesList.getSelectionModel().getSelectedItem());

        try {
          Stage primaryStage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();

          // FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("App.fxml"));
          // Parent mainPane = mainLoader.load();
          // Scene mainScene = new Scene(mainPane);

          // getting loader and a pane for the second scene.
          FXMLLoader courseLoader = new FXMLLoader(getClass().getResource("Course.fxml"));
          Parent coursePane = courseLoader.load();
          Scene courseScene = new Scene(coursePane);

          // injecting first scene into the controller of the second scene
          CourseController courseController = (CourseController) courseLoader.getController();
          // courseController.setMainScene(mainScene);

          // injecting second scene into the controller of the first scene
          CourseItem courseItem = findCourseItem(coursesList.getSelectionModel().getSelectedItem());
          courseController.setCourseItem(courseItem);
          courseController.setCurrentUser(user);
          courseController.updateView();

          primaryStage.setScene(courseScene);
          primaryStage.setTitle("StudIt");
          primaryStage.show();

        } catch (Exception e) {
          System.out.println(e);
        }
      }
    });
  }

  private CourseItem findCourseItem(String name) {
    for (CourseItem courseItem : this.courseList) {
      if (courseItem.getFagnavn().equals(name)) {
        return courseItem;
      }
    }
    return null;
  }

  /**
   * This function should actually fetch data from a database. This will be
   * implemented later.
   * 
   * @throws ApiCallException
   */
  private void loadData() throws ApiCallException {

    CourseList li = remoteStuditModelAccess.getCourseList();

    Collection<CourseItem> items = li.getCourseItems();
    this.courseList = (List<CourseItem>) items;

    // System.out.println(items.size());

    for (CourseItem c : items) {
      this.list.add(c.getFagnavn());
    }

    this.coursesList.setItems(this.list);

  }

  /**
   * This function returns the list of subjects.
   * 
   * @return list;
   */
  public ObservableList<String> getData() {
    return (ObservableList<String>) list;
  }

}
