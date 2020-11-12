package studit.ui.chatbot;

import static studit.core.chatbot.InformationRequestExecutor.executeCommand;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import studit.core.chatbot.Response;
import studit.core.chatbot.prompt.ResponseManager;
import studit.ui.AppController;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.RemoteStuditModelAccess;

public class ChatbotController implements Initializable {

  private Stage stage = null;
  private double xOffset = 0;
  private double yOffset = 0;
  public static final int lineBreakLength = 48;
  public ResponseManager responseManager;
  private ChatbotController chatbotController;
  private RemoteStuditModelAccess remoteAccess = new RemoteStuditModelAccess();
  private boolean displayContent = true;

  public Commands commands;

  /**
   * Called when a new controller instance is created.
   * 
   * @param location  default
   * @param resources default
   */
  @Override
  public void initialize(URL location, ResourceBundle resources) {

    chatbotController = this;

    // Create new Commands instance and ResponseManager
    commands = new Commands(chatbotController, remoteAccess);
    responseManager = new ResponseManager();

    // Add a listener to the text entry of the chatbot
    txt_user_entry.textProperty().addListener(l -> checkForLineBreak());

    ObservableList<Message> chatMessages = FXCollections.observableArrayList();

    list_chat.setItems(chatMessages); // attach the observable list to the listview

    // Initialize all UI logic for the listcell items.
    list_chat.setCellFactory(param -> {

      ListCell<Message> cell = new ListCell<Message>() {
        Label lblUserLeft = new Label();
        TextFlow flowTextLeft = new TextFlow();
        HBox hBoxLeft = new HBox(lblUserLeft, flowTextLeft);

        Label lblUserRight = new Label();
        Label lblTextRight = new Label();
        HBox hBoxRight = new HBox(lblTextRight, lblUserRight);

        {
          // Make sure that there is enough padding between the cells
          hBoxLeft.setAlignment(Pos.CENTER_LEFT);
          hBoxRight.setAlignment(Pos.CENTER_RIGHT);
          hBoxRight.setPadding(new Insets(5, 0, 5, 0));
          hBoxLeft.setPadding(new Insets(5, 0, 5, 10));
        }

        // This function is called whenever the list_chat is modified.
        @Override
        protected void updateItem(Message item, boolean empty) {
          if (displayContent) {
            super.updateItem(item, empty); // Call super to prevent update complications.

            if (empty) { // We have nothing to display.
              setText(null);
              setGraphic(null);
            } else {
              if (item.getUser().equals("chatbot")) {
                // Chatbot response, style the TextFlow accordingly.
                flowTextLeft.setStyle("-fx-background-color: linear-gradient(to left, #ff512f, #dd2476);\r\n"
                    + "    -fx-background-insets: -5 -30 -5 -15;\r\n"
                    + "    -fx-effect: dropshadow(three-pass-box,rgba(0,0,0,0.08),2,1.0,0.5,0.5);\r\n"
                    + "    -fx-shape: \"M 94.658379,129.18587 H 46.277427 c -3.545458,0.23354 -5.32763,-1.59167 -5.14193,-4.67449\r\n"
                    + "    v -19.39913 c 0.405797,-3.73565 2.470637,-4.56641 5.14193,-4.90821 h 43.706464 c 2.572701,0.2361 4.604321,\r\n"
                    + "    1.68288 4.674488,4.90821 v 19.39913 c 0.436089,3.14572 2.890695,3.57304 4.908212,4.67449 z\";");
                flowTextLeft.getChildren().clear();

                Text text = new Text(item.getText());
                text.setFill(Color.WHITE);

                // We want the user to choose between a certain set of options, initializes a
                // new prompt.
                if (item.getPrompt() != null) {
                  text.setText(text.getText() + "\n");
                  flowTextLeft.getChildren().add(text);
                  new Prompt(item.getPrompt(), item.getArgs1(), item.getArgs2(), flowTextLeft, list_chat, item,
                      chatbotController);
                } else {
                  flowTextLeft.getChildren().add(text);
                }
                setGraphic(hBoxLeft);
              } else {
                // User message, style the TextFlow accordingly.
                lblTextRight.setStyle("-fx-background-color: linear-gradient(to left, #4776e6, #8e54e9);\r\n"
                    + "    -fx-background-insets: -5 -5 -5 -34;\r\n"
                    + "    -fx-effect: dropshadow(three-pass-box,rgba(0,0,0,0.08),2,1.0,-0.5,-0.5);\r\n"
                    + "    -fx-shape: \"m 46.030545,129.18592 h 48.380952 c 3.54546,0.23355 5.32763,-1.59167 5.14193,-4.67449\r\n"
                    + "    V 105.1123 c -0.4058,-3.73565 -2.47064,-4.56641 -5.14193,-4.90821 H 50.705033\r\n"
                    + "    c -2.572701,0.2361 -4.604321,1.68288 -4.674488,4.90821 v 19.39913\r\n"
                    + "    c -0.436089,3.14572 -2.890695,3.57304 -4.908212,4.67449 z\";");
                lblTextRight.setText(item.getText());
                setGraphic(hBoxRight);
              }
            }
          }

        }

      };

      return cell;
    });

    // Default message when creating opening a new Chatbot window.
    list_chat.getItems()
        .add(new Message("Hei! Jeg er din nye assistent, chatbotten Gunnar. Hva kan jeg hjelpe deg med?", "chatbot"));
  }

  /**
   * Checks if wee need to add a line break to the user input to avoid text out of
   * bounds.
   */
  private void checkForLineBreak() {

    if (txt_user_entry.getText().length() % lineBreakLength == 0) {
      txt_user_entry.setText(txt_user_entry.getText() + "\n");
    }

  }

  // ----------------------- Member Initialization --------------------------

  @FXML
  private BorderPane pane_chatbot;

  @FXML
  private Button btn_minimize;

  @FXML
  private Button btn_exit;

  @FXML
  ListView<Message> list_chat;

  @FXML
  private TextArea txt_user_entry;

  // ------------------------------ Widget Logic -------------------------------

  /**
   * Exit the chatbot gracefully.
   * 
   * @param event ActionEvent
   */
  @FXML
  public void exitChatbot(ActionEvent event) {
    AppController.closeChatbot();
    try {
      stage.close();
    } catch (IllegalStateException e) {
      System.out.println("Warning -> Application can only be exited from UI thread");
    }
  }

  /**
   * Minimize the chatbot window.
   * 
   * @param event ActionEvent
   */
  @FXML
  public void minimizeChatbot(ActionEvent event) {
    stage.setIconified(true);
  }

  /**
   * Moves the chatbot window when dragged by the toolbar.
   * 
   * @param event MouseEvent
   */
  @FXML
  private void moveWindow(MouseEvent event) {
    stage.setX(event.getScreenX() - xOffset);
    stage.setY(event.getScreenY() - yOffset);
  }

  /**
   * Sets new cursor location (x & yOffset) so that we can drag our application by
   * the toolbar.
   *
   * @param event MouseEvent
   */
  @FXML
  private void setOffset(MouseEvent event) {
    xOffset = event.getSceneX();
    yOffset = event.getSceneY();
  }

  /**
   * When user presses enter key, send the input to the Chatbot to perform an
   * action accordingly.
   * 
   * @param event KeyEvent
   */
  @FXML
  private void userEntry(KeyEvent event) {
    // The user pressed Enter.
    if (event.getCode() == KeyCode.ENTER) {
      String userInput = txt_user_entry.getText();
      txt_user_entry.setText("");
      list_chat.getItems().add(new Message(userInput, "user"));

      txt_user_entry.selectPositionCaret(0); // Reset the caret position.

      // Formulate chatbot response based on core logic.
      Response response = AppController.getChatbot().manageInput(userInput);
      if (response.funcCall()) {
        // We need to execute a command before receiving a chatbot response, typically
        // an API call. This updates the Response object with the new message.
        try {
          executeCommand(response, remoteAccess.getCourseList());
        } catch (ApiCallException e) {
          response.add("Error -> could not establish connection to server");
        }
      }
      list_chat.getItems().add(new Message(response, "chatbot"));
    }
  }

  /**
   * Used for testing purposes only.
   * 
   * @param remoteAccess the remoteAccess to set
   */
  public void setRemoteAccess(RemoteStuditModelAccess remoteAccess) {
    this.remoteAccess = remoteAccess;
  }

  /**
   * Gets the current stage.
   * 
   * @return the stage.
   */
  public Stage getStage() {
    return stage;
  }

  public void setStage(Stage chatStage) {
    this.stage = chatStage;
  }

  /**
   * Set weather we want our listview to automatically update.
   * 
   * @param displayContent the displayContent to set
   */
  public void setDisplayContent(boolean displayContent) {
    this.displayContent = displayContent;
  }

  /**
   * Set a the Commands object for the controller.
   * 
   * @param commands the commands to set
   */
  public void setCommands(Commands commands) {
    this.commands = commands;
  }

}
