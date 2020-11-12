package studit.core.chatbot.prompt;

import java.util.ArrayList;
import java.util.List;

public class ActionRequest {

  private String chatbotResponse;
  private String funcKey;
  private List<Object> arguments;

  /**
   * Initialize empty ActionRequest.
   */
  public ActionRequest() {
    arguments = new ArrayList<>();
  }

  /**
   * Add a function argument to the ActionRequest.
   * 
   * @param arg argument of any type extending Object. See Commands class in
   *            studit/ui/chatbot for more information.
   */
  public void addArgument(Object arg) {
    arguments.add(arg);
  }

  /**
   * Get the current planned Chatbot response.
   * 
   * @return String containing unformatted chatbot response.
   */
  public String getChatbotResponse() {
    return chatbotResponse;
  }

  /**
   * Set the chatbotresponse.
   * 
   * @param chatbotResponse unformatted string containing requested chatbot
   *                        response.
   */
  public void setChatbotResponse(String chatbotResponse) {
    this.chatbotResponse = chatbotResponse;
  }

  /**
   * Get the function arguments.
   * 
   * @return List of object containing our function arguments.
   */
  public List<Object> getArguments() {
    return arguments == null ? new ArrayList<Object>() : arguments;
  }

  /**
   * Set the function arguments.
   * 
   * @param arguments list containing function arguments.
   */
  public void setArguments(List<Object> arguments) {
    this.arguments = arguments;
  }

  /**
   * Get the currently active funckey (to be executed in
   * studit/ui/chatbot/Commands).
   * 
   * @return funckey (HashMap key)
   */
  public String getFuncKey() {
    return funcKey;
  }

  /**
   * Set the function key.
   * 
   * @param funcKey HashMap key for the desired function to execute
   */
  public void setFuncKey(String funcKey) {
    this.funcKey = funcKey;
  }

}
