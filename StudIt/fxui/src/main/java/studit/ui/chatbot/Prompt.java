package studit.ui.chatbot;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import studit.core.chatbot.prompt.ActionRequest;
import studit.core.chatbot.prompt.Func;
import studit.core.chatbot.prompt.PromptManager;
import studit.ui.ChatbotController;

public class Prompt {

  private List<Hyperlink> commands;
  private ListView<Message> listChat;
  private PromptManager responseManager;
  private ChatbotController chatbotController;

  public Prompt(List<String[]> prompts, TextFlow text, ListView<Message> listChat, Message message,
      ChatbotController chatbotController) {

    this.commands = new ArrayList<>();
    this.listChat = listChat;
    this.responseManager = chatbotController.promptManager;
    this.chatbotController = chatbotController;

    // Assign commands to each of the options
    for (String[] prompt : prompts) {
      Hyperlink command = new Hyperlink(prompt[0]);
      // If the user has already clicked an option, make sure that we disable further clicks
      if (!message.isClicked()) {
        command.setOnAction(e -> {
          handleHyperlinkClick(prompt[1]);
          message.click();
        });
      } else {
        command.setOnAction(null);
      }
      text.getChildren().addAll(command, new Text("  "));
      commands.add(command);
    }
  }

  /**
   * Handles event where user has selected one of the given chatbot options.
   * 
   * @param option user selected option
   */
  private void handleHyperlinkClick(String option) {

    ActionRequest action = responseManager.handlePrompt(option);
    String result = action.getChatbotResponse();

    if (!result.isEmpty()) {
      listChat.getItems().add(new Message(result, "chatbot"));
    }

    Func func = chatbotController.commands.getCommands().get(action.getFuncKey());

    if (func != null) {

      try {
        func.execute(action.getArguments());
      } catch (ClassCastException e) {
        e.printStackTrace();
        System.out.println("^ Error occured casting function arguments from '" + action.getFuncKey()
            + "'. Update code immediately");
      }
    }

    // Make sure all commands are disable if the ListView is not updated (does not perform isClicked
    // check)
    for (Hyperlink command : commands) {
      command.setOnAction(null);
    }

  }

}
