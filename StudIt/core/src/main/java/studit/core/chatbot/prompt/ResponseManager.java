package studit.core.chatbot.prompt;

public class ResponseManager {

  /**
   * Handles the response when user has picked a chatbot option from the prompt.
   * 
   * @param command String defined in CommandManager
   * @return appropriate Chatbot response
   */
  public ActionRequest handlePrompt(String command) {
    
    ActionRequest action = new ActionRequest();

    String result = "";

    switch (command) {
      case "regret":
        result = "Den er god du!";
        break;
      case "exit":
        result = "Avslutter chatbot...";
        action.setFuncKey("exit");
        break;
      case "faginfo": 
        action.setFuncKey("faginfo");
        break;
      case "fagoversikt":
        action.setFuncKey("fagoversikt");
      default:
        result = "";
    }
    
    action.setChatbotResponse(result);

    return action;
  }

}
