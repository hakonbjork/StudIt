package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;

public class DataMatcher {

  private List<String> wordsToExclude;
  private List<String[]> courseNameList;

  public DataMatcher(List<String> wordsToExclude, List<String[]> courseNameList) {
    this.wordsToExclude = wordsToExclude;
    this.courseNameList = courseNameList;
    for (String[] courseName : courseNameList) {
      courseName[0] = courseName[0].toLowerCase();
      courseName[1] = courseName[1].toLowerCase();
    }

  }

  /**
   * Finds a match between the user input and the dataMatch requested.
   * 
   * @param userInput Array of user input separted by spaces.
   * @param dataMatch used to identify which match we are trying to find, e.g
   *                  "course" if we want to match a course.
   * @return String[], where String[0] is the match, or null if no match was
   *         found. String[1] is null if no plausible match is found, otherwise
   *         plausible match.
   */
  public String[] findDataMatch(String[] userInput, String dataMatch) {
    List<String> matchCandidates = new ArrayList<>();

    for (String word : userInput) {
      if (!wordsToExclude.contains(word)) {
        matchCandidates.add(word);
      }
    }

    if (matchCandidates.isEmpty()) {
      return null;
    }

    switch (dataMatch) {
      case "course":
        return matchCourse(matchCandidates);
      default:
        return null;
    }
  }

  /**
   * Check for course matches with the given candidates.
   * 
   * @param matchCandidates list of possible matches.
   * @return String[], where String[0] is the match, or null if no match was
   *         found. String[1] is null if no plausible match is found, otherwise
   *         plausible match.
   */
  private String[] matchCourse(List<String> matchCandidates) {

    String bestMatch = "";
    float matchPct = 0.0f;

    for (String candidate : matchCandidates) {
      for (String[] courseName : courseNameList) {
        // Find match percentages.
        float match1 = matchWords(courseName[0], candidate);
        float match2 = matchWords(courseName[1], candidate);

        // Update greatest match if found.
        if (match1 >= matchPct) {
          matchPct = match1;
          bestMatch = courseName[0];
        } 
        if (match2 >= matchPct) {
          matchPct = match2;
          bestMatch = courseName[1];
        }
      }

    }
    System.out.println(bestMatch + " " + matchPct);
    return new String[] { matchPct == 1.0f ? bestMatch : null, matchPct > 0.6f ? bestMatch : null };
  }

  /**
   * Matches two strings.
   * 
   * @param word1 one of the words.
   * @param word2 the other word.
   * @return float between 0 and 1, where 0 is no match, and 1 is 100% match.
   */
  private float matchWords(String word1, String word2) {
    int unions = 0;
    int complements = 0;

    StringBuffer tmpWord = new StringBuffer(word2);

    System.out.println("Mathing: " + word1 + " with " + word2);
    
    for (int i = 0; i < word1.length(); i++) {
      char c = word1.charAt(i);

      int matchIdx = tmpWord.indexOf(String.valueOf(c));
      if (matchIdx >= 0) {
        unions++;
        tmpWord.deleteCharAt(matchIdx);
      } else {
        complements++;
      }
    }

    float pct = unions / (float) (unions + complements);
    pct *= (word2.length() - Math.abs(word1.length() - word2.length())) / (float) word2.length();
    System.out.println(pct);
    return pct;
  }

}