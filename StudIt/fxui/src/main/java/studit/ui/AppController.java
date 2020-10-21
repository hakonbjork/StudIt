package studit.ui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import studit.core.chatbot.Chatbot;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.json.CoursePersistence;

public class AppController {

  @FXML
  private ListView<CourseItem> coursesList;
  @FXML
  private Button mainPageAction;
  @FXML
  private Button openChatBot;
  @FXML
  private Button ntnuAction;
  @FXML
  private Button logoutAction;
  @FXML
  public BorderPane rootPane;
  @FXML
  private AnchorPane mainPane;
  @FXML
  private TextField searchField;
  @FXML
  private Button mainPage_btn;
  @FXML
  private Button chatbot_btn;
  @FXML
  private Button ntnu_btn;
  @FXML
  private Button logout_btn;

  static Chatbot chatbot = null;
  private ObservableList<CourseItem> list = FXCollections.observableArrayList();
  private List<CourseItem> courseList;
  private String label;
  private CoursePersistence coursePersistence = new CoursePersistence();

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

  /**
   * Function to initialize AppController.
   */
  public void initialize() {
    loadData();
    coursesList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
    // Actions on clicked list item
    mouseClicked();
    // searchField.textProperty().addListener(new
    // ChangeListener<String>.changed(ObservableValue<? extends String>, String,
    // String) {
    // @Override
    // public void changed(ObservableValue observable, Object oldValue, Object
    // newValue) {
    // filterCoursesList((String) oldValue, (String) newValue);
    // }
    // });
  }

  /**
   * Function to search for subjects. The listview will then only show subjects
   * with the letters in the search field.
   */
  @FXML
  public void handleSearchViewAction() {
    // Wrap the ObservableList in a FilteredList (initially display all data).
    FilteredList<CourseItem> filteredData = new FilteredList<>(getData(), p -> true);

    // Set the filter Predicate whenever the filter changes.
    searchField.textProperty().addListener((observable, oldValue, newValue) -> {
      filteredData.setPredicate(courseItem -> {
        // If filter text is empty, display all persons.
        if (newValue == null || newValue.isEmpty()) {
          return true;
        }

        // Compare course name and course code of every CourseItem with the filter text.
        String lowerCaseFilter = newValue.toLowerCase();

        if (courseItem.getFagnavn().toLowerCase().contains(lowerCaseFilter)) {
          return true; // filter matches course name
        } else if (courseItem.getFagkode().toLowerCase().contains(lowerCaseFilter)) {
          return true; // filter matches course code
        }
        return false; // Does not match
      });
    });

    // Wrap the FilteredList in a SortedList.
    SortedList<CourseItem> sortedData = new SortedList<>(filteredData);

    // put the sorted list into the listview
    coursesList.setItems(sortedData);

    coursesList.setCellFactory(new Callback<ListView<CourseItem>, ListCell<CourseItem>>() {
      @Override
      public ListCell<CourseItem> call(ListView<CourseItem> param) {
        final Label leadLbl = new Label();
        final Tooltip tooltip = new Tooltip();
        final ListCell<CourseItem> cell = new ListCell<CourseItem>() {
          @Override
          public void updateItem(CourseItem item, boolean empty) {
            super.updateItem(item, empty);
            if (item != null) {
              leadLbl.setText(item.getFagkode());
              setText(item.getFagnavn() + " " + item.getFagnavn());
              tooltip.setText(item.getFagkode());
              setTooltip(tooltip);
            }
          }
        };
        return cell;
      }
    });
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
   * Should give the option to go to the subjects web-page.
   */
  @FXML
  void handleNtnuAction(ActionEvent event) {

    // go to NTNU homepage (question if you want to open web-browser)?
    // or a new window with information about NTNU?

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
   * redirects user to the main page.
   */
  @FXML
  void handleMainPageAction() {
    // nothing should really happen when you are in the home page other than maybe
    // refresh(?)
  }

  /**
   * A function that does something when a element in the listview is clicked on.
   */
  public void mouseClicked() {
    // Detecting mouse clicked
    coursesList.setOnMouseClicked(new EventHandler<MouseEvent>() {
      // private String label;
      @Override
      public void handle(MouseEvent arg0) {
        // System.out.println((coursesList.getSelectionModel().getSelectedItem()));
        setLabel(coursesList.getSelectionModel().getSelectedItem().getFagnavn());

        try {
          Stage primaryStage = (Stage) ((Node) arg0.getSource()).getScene().getWindow();

          FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("App.fxml"));
          Parent mainPane = mainLoader.load();
          Scene mainScene = new Scene(mainPane);

          // getting loader and a pane for the second scene.
          FXMLLoader courseLoader = new FXMLLoader(getClass().getResource("Course.fxml"));
          Parent coursePane = courseLoader.load();
          Scene courseScene = new Scene(coursePane);
          
          // injecting first scene into the controller of the second scene
          CourseController courseController = (CourseController) courseLoader.getController();
          courseController.setMainScene(mainScene);

          // injecting second scene into the controller of the first scene
          CourseItem courseItem = findCourseItem(coursesList.getSelectionModel().getSelectedItem().getFagnavn());
          courseController.setCourseText(courseItem.getInformasjon());
          courseController.setLabel(coursesList.getSelectionModel().getSelectedItem().getFagnavn().substring(0, 8));

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
   */
  private void loadData() {

    try (FileReader fr = new FileReader("src/main/resources/studit/db/db.json", StandardCharsets.UTF_8)) {

      CourseList li = coursePersistence.readCourseList(fr);

      // System.out.println(li.getCourseItems().size());

      Collection<CourseItem> items = li.getCourseItems();
      this.courseList = (List<CourseItem>) items;

      // System.out.println(items.size());

      for (CourseItem c : items) {
        this.list.add(c);
      }

      this.coursesList.setItems(this.list);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  /**
   * This function returns the list of subjects.
   * 
   * @return list;
   */
  public ObservableList<CourseItem> getData() {
    return (ObservableList<CourseItem>) list;
  }

}
