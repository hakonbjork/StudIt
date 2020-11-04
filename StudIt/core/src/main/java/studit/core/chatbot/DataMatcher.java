package studit.core.chatbot;

import java.util.List;

public class DataMatcher {

  private List<String> wordsToExclude;

  public DataMatcher(List<String> wordsToExclude) {
    this.wordsToExclude = wordsToExclude;
  }

  /** Get words to exclude.
   * @return the wordsToExclude
   */
  public List<String> getWordsToExclude() {
    return wordsToExclude;
  }

  /** Set words to exclude.
   * @param wordsToExclude the wordsToExclude to set
   */
  public void setWordsToExclude(List<String> wordsToExclude) {
    this.wordsToExclude = wordsToExclude;
  }

}