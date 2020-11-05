package studit.ui.chatbot;

import java.util.HashMap;
import java.util.List;

import studit.core.chatbot.prompt.Func;
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
  }

  public HashMap<String, Func> getCommands() {
    return commands;
  }

  // ------------------------------------COMMANDS----------------------------------

  private void exit() {
    chatbotController.exitChatbot(null);
  }

  private void faginfo(List<Object> args) {
    if (args.get(0) != null) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message("Her kommer litt informasjon om " + fagkode + ": "
            + remoteAccess.getCourseByFagkode(fagkode).getInformasjon(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems().add(new Message("Could not establish connection to server", "chatbot"));
      }
    }
  }
}
