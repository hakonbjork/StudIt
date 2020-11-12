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

    // Switch the command for the different chatbot response options.
    switch (command) {
      case "regret":
        result = "Den er god du!";
        break;
      case "exit":
        result = "Avslutter chatbot...";
        action.setFuncKey("exit");
        break;
      default:
        /*
         * If command is not listed, this means that we want to execute a function in
         * ChatbotController that determines what the chatbot response should be. Thus,
         * we set the response to blank for now, and set the function key s.t the
         * ChatbotController knows how to further process the request.
         */
        result = "";
        action.setFuncKey(command);
    }

    action.setChatbotResponse(result);

    return action;
  }

}
