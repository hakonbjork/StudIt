package studit.core.chatbot;

import java.util.List;

public class Response {
  
  private String response = "";
  private List<String[]> prompt;

  public void add(String addition) {
    if (!addition.isEmpty()) {
      this.response += addition;
    }
  }

  /** Get the response.
   * @return the response
   */
  public String getResponse() {
    return response;
  }

  /** Set the response.
   * @param response the response to set
   */
  public void setResponse(String response) {
    this.response = response;
  }

  /** Get the prompt.
   * @return the prompt
   */
  public List<String[]> getPrompt() {
    return prompt;
  }

  /** Set the prompt.
   * @param prompt the prompt to set
   */
  public void setPrompt(List<String[]> prompt) {
    this.prompt = prompt;
  }
  
}
