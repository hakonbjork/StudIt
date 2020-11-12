package studit.ui.chatbot;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import studit.core.chatbot.prompt.ActionRequest;
import studit.core.chatbot.prompt.Func;
import studit.core.chatbot.prompt.ResponseManager;

public class Prompt {

  private List<Hyperlink> commands;
  private ListView<Message> listChat;
  private ResponseManager responseManager;
  private ChatbotController chatbotController;

  public Prompt(List<String[]> prompts, List<Object> args1, List<Object> args2, TextFlow text,
      ListView<Message> listChat, Message message, ChatbotController chatbotController) {

    this.commands = new ArrayList<>();
    this.listChat = listChat;
    this.responseManager = chatbotController.promptManager;
    this.chatbotController = chatbotController;

    boolean first = false;
    // Assign commands to each of the options
    for (String[] prompt : prompts) {
      Hyperlink command = new Hyperlink(prompt[0]);
      // If the user has already clicked an option, make sure that we disable further
      // clicks
      if (!message.isClicked()) {
        if (!first) {
          command.setOnAction(e -> {
            handleHyperlinkClick(prompt[1], args1);
            message.click();
          });
        } else {
          command.setOnAction(e -> {
            handleHyperlinkClick(prompt[1], args2);
            message.click();
          });
        }

      } else {
        command.setOnAction(null);
      }
      text.getChildren().addAll(command, new Text("  "));
      commands.add(command);

      first = true;
    }
  }

  /**
   * Handles event where user has selected one of the given chatbot options.
   * 
   * @param option user selected option
   */
  private void handleHyperlinkClick(String option, List<Object> args) {

    ActionRequest action = responseManager.handlePrompt(option);

    action.setArguments(args);
    String result = action.getChatbotResponse();

    if (!result.isEmpty()) {
      listChat.getItems().add(new Message(result, "chatbot"));
    }

    Func func = chatbotController.commands.getCommands().get(action.getFuncKey());
    func.execute(action.getArguments());

    // Make sure all commands are disabled if the ListView is not updated (does not
    // perform isClicked check)
    for (Hyperlink command : commands) {
      command.setOnAction(null);
    }

  }

}
