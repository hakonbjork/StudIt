package studit.core.chatbot;

import java.util.List;

public class Response {
  
  public String response = "";
  public List<String[]> prompt;
  
  public void add(String addition) {
    if (!addition.isEmpty()) {
      this.response += addition;
    }
  }
  
}
