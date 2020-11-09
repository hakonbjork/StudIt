package studit.ui.chatbot;

import java.util.HashMap;
import java.util.List;
import studit.core.chatbot.prompt.Func;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.RemoteStuditModelAccess;

public class Commands {
  private HashMap<String, Func> commands;
  private ChatbotController chatbotController;
  private RemoteStuditModelAccess remoteAccess;

  public Commands(ChatbotController chatbotController, RemoteStuditModelAccess remoteAccess) {
    this.chatbotController = chatbotController;
    this.remoteAccess = remoteAccess;
    commands = new HashMap<>();
    initializeCommands();
  }

  private void initializeCommands() {
    commands.put("exit", (args) -> exit());
    commands.put("faginfo", (args) -> faginfo(args));
    commands.put("fagoversikt", (args) -> fagoversikt());
  }

  public HashMap<String, Func> getCommands() {
    return commands;
  }

  /**
   * This is called whenever user clicks the "nei" option in the prompt and we
   * guessed the wrong course. Pring generic message to the user.
   */
  private void courseGuessDeclined() {
    chatbotController.list_chat.getItems()
        .add(new Message("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt.", "chatbot"));
  }

  // ------------------------------------COMMANDS----------------------------------

  private void exit() {
    chatbotController.exitChatbot(null);
  }

  private void faginfo(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message("Her kommer litt informasjon om " + fagkode + ": "
            + remoteAccess.getCourseByFagkode(fagkode).getInformasjon(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems().add(new Message("Could not establish connection to server", "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }

  }

  private void fagoversikt() {
    try {
      CourseList courseList = remoteAccess.getCourseList();
      StringBuffer buffer = new StringBuffer(
          String.format("Fagoversikt:%1$" + (ChatbotController.lineBreakLength - 20) + "s", ""));
      for (CourseItem courseItem : courseList) {
        String str = courseItem.getFagkode() + " - " + courseItem.getFagnavn();
        buffer.append(String.format(str + "%1$" + (ChatbotController.lineBreakLength - 8 - str.length()) + "s", ""));
      }
      chatbotController.list_chat.getItems().add(new Message(buffer.toString(), "chatbot"));
    } catch (ApiCallException e) {
      chatbotController.list_chat.getItems().add(new Message("Could not establish connection to server", "chatbot"));
    }
  }
}
