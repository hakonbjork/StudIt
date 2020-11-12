package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KeywordLink {

  private String command;
  private List<Map<String, Float>> keywordLinksList;
  private int precedence;
  private String dataMatch;

  // Default constructor for serialization purposes.
  public KeywordLink() {

  }

  /**
   * Initalize a new KeywordLink
   * 
   * @param command          command key.
   * @param dataMatch        datamatch key if we request a datamatch, empty
   *                         otherwise.
   * @param precedence       In what order should we execute the command? 1 =
   *                         first, 3 = last.
   * @param keywordLinksList list of keywordlinks.
   */
  public KeywordLink(String command, String dataMatch, int precedence, List<Map<String, Float>> keywordLinksList) {
    this.command = command;
    this.dataMatch = dataMatch;
    this.keywordLinksList = keywordLinksList;
    this.setPrecedence(precedence);
  }

  /**
   * Get the command key.
   * 
   * @return the command key.
   */
  public String getCommand() {
    return command;
  }

  /**
   * Set the command key.
   * 
   * @param command the command key.
   */
  public void setCommand(String command) {
    this.command = command;
  }

  /**
   * Get the list of keywords.
   * 
   * @return the list of keywords.
   */
  public List<Map<String, Float>> getKeywords() {
    return keywordLinksList;
  }

  /**
   * Set the list of keywords.
   * 
   * @param keywords the list of keywords.
   */
  public void setKeywords(List<Map<String, Float>> keywords) {
    this.keywordLinksList = keywords;
  }

  /**
   * Get a list of all words found in the keywordLinksList.
   * 
   * @return all recognized words.
   */
  public List<String> getWords() {
    List<String> words = new ArrayList<>();
    for (Map<String, Float> keyword : keywordLinksList) {
      words.addAll(keyword.keySet());
    }

    return words;
  }

  /**
   * Get the precedence of the KeywordLink.
   * 
   * @return The precedence of the KeywordLink.
   */
  public int getPrecedence() {
    return precedence;
  }

  /**
   * Set the precedence of the KeywordLink.
   * 
   * @param precedence The precedence of the KeywordLink.
   */
  public void setPrecedence(int precedence) {
    this.precedence = precedence;
  }

  /**
   * Get the datamatch key.
   * 
   * @return the datamatch key
   */
  public String getDataMatch() {
    return dataMatch;
  }

  /**
   * Set the datamatch key.
   * 
   * @param dataMatch get the datamatch key.
   */
  public void setDataMatch(String dataMatch) {
    this.dataMatch = dataMatch;
  }

}
