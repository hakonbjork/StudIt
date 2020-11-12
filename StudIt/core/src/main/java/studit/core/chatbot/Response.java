package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;

public class Response {

  private String response = "";
  private String funcKey = "null";

  private List<String[]> prompt;
  private List<String> arguments = new ArrayList<>();
  private List<Object> args1;
  private List<Object> args2;

  private boolean callFunc = false;

  /**
   * Add a new message to the chatbot response.
   * 
   * @param addition new message to be added.
   */
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
  public void setPrompt(List<String[]> prompt, List<Object> args1, List<Object> args2) {
    this.prompt = prompt;
    this.args1 = args1;
    this.args2 = args2;
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

  /**
   * Get the first prompt arguments.
   * 
   * @return the args1
   */
  public List<Object> getArgs1() {
    return args1;
  }

  /**
   * Get the second prompt arguments.
   * 
   * @return the args2
   */
  public List<Object> getArgs2() {
    return args2;
  }


}
