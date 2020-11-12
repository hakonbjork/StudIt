package studit.ui.chatbot;

import java.util.List;
import studit.core.chatbot.Response;

public class Message {

  private String text;
  private String user;
  private List<String[]> prompt = null;
  private List<Object> args1 = null;
  private List<Object> args2 = null;
  private boolean clicked;

  /**
   * Create a new Message instance.
   * 
   * @param response active Repsose object.
   * @param user     either "chatbot" or "user"
   */
  public Message(Response response, String user) {
    this.text = response.getResponse();
    this.prompt = response.getPrompt();
    this.user = user;
    this.clicked = false;
    this.args1 = response.getArgs1();
    this.args2 = response.getArgs2();
  }

  /**
   * Intialize new Message instance with simple response String.
   * 
   * @param response message to print to the user.
   * @param user     either "chatbot" or "user"
   */
  public Message(String response, String user) {
    this.text = response;
    this.user = user;
  }

  /**
   * Returns formatted text with correct line breaks.
   * 
   * @return String ready to be printed to the screen.
   */
  public String getText() {

    String[] words = text.replace("\n", "").split(" ");

    StringBuffer line = new StringBuffer();
    StringBuffer output = new StringBuffer();

    for (String word : words) {
      // %S indicates we want a newline char
      if (word.equals("%S")) {
        output.append(line + "\n");
        line = new StringBuffer();
      } else {
        if (line.length() + word.length() > ChatbotController.lineBreakLength - 8) {
          line.append('\n');
          output.append(line);
          line = new StringBuffer(word + " ");
        } else {
          line.append(word + " ");
        }
      }
    }

    output.append(line);

    return output.toString();

  }

  /**
   * Set the text.
   * 
   * @param text the text to set.
   */
  public void setText(String text) {
    this.text = text;
  }

  /**
   * Get the user
   * 
   * @return active user.
   */
  public String getUser() {
    return user;
  }

  /**
   * Set the prompt.
   * 
   * @param prompt the prompt to set.
   */
  public void setPromt(List<String[]> prompt) {
    this.prompt = prompt;
  }

  /**
   * Get the current prompt.
   * 
   * @return the current prompt.
   */
  public List<String[]> getPrompt() {
    return this.prompt;
  }

  /**
   * Check weather a user har clicked on a prompt option.
   * 
   * @return true if user has clicked a prompt option, false otherwise.
   */
  public boolean isClicked() {
    return clicked;
  }

  /**
   * Called whenever a user has clicked on a prompt option.
   */
  public void click() {
    this.clicked = true;
  }

  /**
   * Get args for first prompt action.
   * 
   * @return the args1
   */
  public List<Object> getArgs1() {
    return args1;
  }

  /**
   * Get args for second prompt action.
   * 
   * @return the args2
   */
  public List<Object> getArgs2() {
    return args2;
  }

}
