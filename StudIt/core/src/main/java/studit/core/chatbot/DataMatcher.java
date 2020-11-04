package studit.core.chatbot;

import static studit.core.Commons.contains;

import java.util.ArrayList;
import java.util.List;

public class DataMatcher {

  private List<String> matchCandidates;

  public DataMatcher(String dataMatch, String[] words, String[] wordsToExclude) {
    matchCandidates = new ArrayList<>();
    for (String word : words) {
      if (!contains(wordsToExclude, word)) {
        matchCandidates.add(word);
      }
    }

  }

  public String findMatch() {
    return null;
  }

  /**
   * Get macth candidate.
   * 
   * @return the matchCandidates.
   */
  public List<String> getMatchCandidates() {
    return matchCandidates;
  }

  /**
   * Set match candidate.
   * 
   * @param matchCandidates the matchCandidates to set
   */
  public void setMatchCandidates(List<String> matchCandidates) {
    this.matchCandidates = matchCandidates;
  }

}