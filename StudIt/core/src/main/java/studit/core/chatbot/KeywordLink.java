package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeywordLink {

  private String command;
  private List<Map<String, Float>> keywordLinksList;
  private int precedence;
  private String dataMatch;

  public KeywordLink() {

  }

  public KeywordLink(String command, String dataMatch, int precedence, List<Map<String, Float>> keywordLinksList) {
    this.command = command;
    this.dataMatch = dataMatch;
    this.keywordLinksList = keywordLinksList;
    this.setPrecedence(precedence);
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

  public String getDataMatch() {
    return dataMatch;
  }

  public void setDataMatch(String dataMatch) {
    this.dataMatch = dataMatch;
  }


}
