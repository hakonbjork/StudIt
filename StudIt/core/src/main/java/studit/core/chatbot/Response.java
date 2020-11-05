package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;

public class Response {

  private String response = "";
  private List<String[]> prompt;
  private String funcKey = "null";
  private List<String> arguments = new ArrayList<>();
  private boolean callFunc = false;

  public void add(String addition) {
    if (!addition.isEmpty()) {
      this.response += addition;
    }
  }

  /**
   * Get the response.
   * 
   * @return the response
   */
  public String getResponse() {
    return response;
  }

  /**
   * Set the response.
   * 
   * @param response the response to set
   */
  public void setResponse(String response) {
    this.response = response;
  }

  /**
   * Get the prompt.
   * 
   * @return the prompt
   */
  public List<String[]> getPrompt() {
    return prompt;
  }

  /**
   * Set the prompt.
   * 
   * @param prompt the prompt to set
   */
  public void setPrompt(List<String[]> prompt) {
    this.prompt = prompt;
  }

  public void handleMatchResult(String[] matchResult) {
    callFunc = true;
    for (String arg : matchResult) {
      arguments.add(arg);
    }
  }

  /**
   * Set the function key.
   * 
   * @return the funcKey
   */
  public String getFuncKey() {
    return funcKey;
  }

  /**
   * Get the function key.
   * 
   * @param funcKey the funcKey to set
   */
  public void setFuncKey(String funcKey) {
    this.funcKey = funcKey;
  }

  public boolean funcCall() {
    return this.callFunc;
  }

  /**
   * Return the list of command arguments to be executed in
   * InformationRequestExecutor.
   * 
   * @return the arguments
   */
  public List<String> getArguments() {
    return arguments;
  }

}
