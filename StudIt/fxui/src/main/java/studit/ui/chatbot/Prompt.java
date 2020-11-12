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

  /**
   * Create a new prompt instance to display a set of options to the user.
   * 
   * @param prompts           list of prompts.
   * @param args1             arguments to be passed to the first function.
   * @param args2             arguments to be passed to the second function.
   * @param text              TextFlow instance of the prompt.
   * @param listChat          ListView instance.
   * @param message           Message instance related to the prompt.
   * @param chatbotController active chatbotcontroller instance.
   */
  public Prompt(List<String[]> prompts, List<Object> args1, List<Object> args2, TextFlow text,
      ListView<Message> listChat, Message message, ChatbotController chatbotController) {

    this.commands = new ArrayList<>();
    this.listChat = listChat;
    this.responseManager = chatbotController.responseManager;
    this.chatbotController = chatbotController;

    boolean first = false;
    // Assign commands to each of the options
    for (String[] prompt : prompts) {
      Hyperlink command = new Hyperlink(prompt[0]);
      // If the user has already clicked an option, make sure that we disable further
      // clicks
      if (!message.isClicked()) {
        if (!first) {
          // Set the action of the first prompt option
          command.setOnAction(e -> {
            handleHyperlinkClick(prompt[1], args1);
            message.click();
          });
        } else {
          // Set the action of the second prompt option
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
    
    // Asign proper funcKey and create a new ActionRequest.

    ActionRequest action = responseManager.handlePrompt(option);
    action.setArguments(args);
    String result = action.getChatbotResponse();

    // If we already have a response to display before executing the function, print it.
    if (!result.isEmpty()) {
      listChat.getItems().add(new Message(result, "chatbot"));
    }

    // Get a new Func instance from the Commands instance based on the funcKey.
    Func func = chatbotController.commands.getCommands().get(action.getFuncKey());

    // Execute the function with the proper arguments. See studit/ui/Commands for more info.
    func.execute(action.getArguments());

    // Make sure all commands are disabled if the ListView is not updated (does not
    // perform isClicked check)
    for (Hyperlink command : commands) {
      command.setOnAction(null);
    }

  }

}
