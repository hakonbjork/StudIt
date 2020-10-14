package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeywordLink {

  private String command;
  private List<Map<String, Float>> keywordLinksList;
  private int precedence;
  private List<String> commandParent;
  private String dataMatch;
  private boolean parent;

  public KeywordLink() {

  }

  public KeywordLink(String command, List<String> commandParent, String dataMatch, int precedence,
      List<Map<String, Float>> keywordLinksList) {
    this.command = command;
    this.commandParent = commandParent;
    this.dataMatch = dataMatch;
    this.keywordLinksList = keywordLinksList;
    this.setPrecedence(precedence);
    
    this.parent = commandParent != null;
  }

  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  public List<Map<String, Float>> getKeywords() {
    return keywordLinksList;
  }

  public void setKeywords(List<Map<String, Float>> keywords) {
    this.keywordLinksList = keywords;
  }

  public List<String> getWords() {
    List<String> words = new ArrayList<>();
    for (Map<String, Float> keyword : keywordLinksList) {
      words.addAll(keyword.keySet());
    }

    return words;
  }

  public int getPrecedence() {
    return precedence;
  }

  public void setPrecedence(int precedence) {
    this.precedence = precedence;
  }

  public List<String> getCommandParent() {
    return commandParent;
  }

  public void setCommandParent(ArrayList<String> commandParent) {
    this.commandParent = commandParent;
  }

  public String getDataMatch() {
    return dataMatch;
  }

  public void setDataMatch(String dataMatch) {
    this.dataMatch = dataMatch;
  }

  public boolean hasParent() {
    return parent;
  }

}
