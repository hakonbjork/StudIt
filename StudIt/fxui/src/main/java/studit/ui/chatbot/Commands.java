package studit.ui.chatbot;

import java.util.HashMap;
import studit.core.chatbot.prompt.Func;
import studit.ui.ChatbotController;

public class Commands {
  private HashMap<String, Func> commands;
  private ChatbotController chatbotController;

  public Commands(ChatbotController chatbotController) {
    this.chatbotController = chatbotController;
    commands = new HashMap<>();
    initializeCommands();
  }

  private void initializeCommands() {
    commands.put("exit", (args) -> exit());
  }
  
  public HashMap<String, Func> getCommands() {
    return commands;
  }
 
  // ------------------------------------COMMANDS----------------------------------
  
  private void exit() {
    chatbotController.exitChatbot(null);
  }
}
