package studit.core.chatbot.prompt;

import java.util.ArrayList;
import java.util.List;

public class ActionRequest {
  
  private String chatbotResponse;
  private String funcKey;
  private List<Object> arguments; 
  
  public ActionRequest() {
    arguments = new ArrayList<>();
  }
  
  public void addArgument(Object arg) {
    arguments.add(arg);
  }

  public String getChatbotResponse() {
    return chatbotResponse;
  }

  public void setChatbotResponse(String chatbotResponse) {
    this.chatbotResponse = chatbotResponse;
  }

  public List<Object> getArguments() {
    return arguments;
  }

  public void setArguments(List<Object> arguments) {
    this.arguments = arguments;
  }

  public String getFuncKey() {
    return funcKey;
  }

  public void setFuncKey(String funcKey) {
    this.funcKey = funcKey;
  }
  

}
