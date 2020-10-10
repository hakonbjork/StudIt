package studit.ui.chatbot;

import java.util.List;
import studit.core.chatbot.Response;
import studit.ui.ChatbotController;

public class Message {

  private String text;
  private String user;
  private List<String[]> prompt = null;

  public Message(Response response, String user) {
    this.text = response.response;
    this.prompt = response.prompt;
    this.user = user;
  }
  
  public Message(String response, String user) {
    this.text = response;
    this.user = user;
  }

  /**
   * Returns formatted text with correct line breaks.
   * 
   * @return String ready to be printed to the screen
   */
  public String getText() {

    String[] words = text.replace("\n", "").split(" ");

    String line = "";
    String output = "";

    for (String word : words) {
      if (line.length() + word.length() > ChatbotController.lineBreakLength - 8) {
        output += line + '\n';
        line = word + " ";
      } else {
        line += word + " ";
      }
    }

    output += line;

    return output;

  }

  public void setText(String text) {
    this.text = text;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }
  
  public void setPromt(List<String[]> prompt) {
    this.prompt = prompt;
  }
  
  public List<String[]> getPrompt() {
    return this.prompt;
  }
}
