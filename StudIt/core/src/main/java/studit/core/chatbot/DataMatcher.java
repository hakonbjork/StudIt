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
   * @return String[], String[0] contains status code (can be safly cast to int):
   *         -1 = no match found, 0 = plausible match found, 1 = 100% match found.
   *         String[1] contains the closest match, String[2] contains extra
   *         information (check other class methods for more info)
   */
  public String[] findDataMatch(String[] userInput, String dataMatch) {
    List<String> matchCandidates = new ArrayList<>();

    for (String word : userInput) {
      if (!wordsToExclude.contains(word)) {
        matchCandidates.add(word);
      }
    }

    if (matchCandidates.isEmpty()) {
      return new String[] {"-1", null, null};
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
   * @return String[]. String[0] is the status code of the match: -1 means no
   *         match found, 0 means plausible match found, 1 means 100% match found.
   *         (can be safely cast to int). String[1] and String[2] contains the
   *         fagkode and course name, where String[1] is either the fagkode or
   *         course name, and contains the closest match, where String[2] contains
   *         the corresponding course name / fagkode.
   */
  private String[] matchCourse(List<String> matchCandidates) {

    int bestFagkodeMatchIdx = -1;
    float bestFagkodePct = 0.0f;

    int bestTitleMatchIdx = -1;
    float bestTitlePct = 0.0f;

    for (int i = 0; i < courseNameList.size(); i++) {
      String[] titleFragments = courseNameList.get(i)[1].split(" "); // Split course title by spaces.
      float titlePct = 0.0f;

      for (String candidate : matchCandidates) {

        float fagkodeMatch = matchWords(courseNameList.get(i)[0], candidate);
        if (fagkodeMatch > bestFagkodePct) {
          bestFagkodeMatchIdx = i;
          bestFagkodePct = fagkodeMatch;
        }

        float tmpMaxTitlePct = 0.0f;
        // Iterate over the title fragments to see if we find a match in one of the
        // words, eg "informatikk" or "prosjektarbeid"
        for (String fragment : titleFragments) {
          float match = matchWords(fragment, candidate);
          if (match > tmpMaxTitlePct) {
            tmpMaxTitlePct = match;
          }
        }

        if (tmpMaxTitlePct > 0) {
          titlePct += tmpMaxTitlePct;
        }
      }

      if (titlePct > bestTitlePct) {
        bestTitleMatchIdx = i;
        bestTitlePct = titlePct;
      }
    }

    if (bestFagkodePct > 0.74) {
      return new String[] { bestFagkodePct >= 1.0f ? "1" : "0", courseNameList.get(bestFagkodeMatchIdx)[0],
          courseNameList.get(bestFagkodeMatchIdx)[1] };
    }

    int titleSplitLength = courseNameList.get(bestTitleMatchIdx)[1].split(" ").length;
    float matchPctPerWord = bestTitlePct / (float) titleSplitLength;

    if (matchPctPerWord >= 1.0f) {
      return new String[] { "1", courseNameList.get(bestTitleMatchIdx)[1],
          courseNameList.get(bestTitleMatchIdx)[0] };
    }

    if (titleSplitLength == 1 && matchPctPerWord >= 0.7f || titleSplitLength > 1 && matchPctPerWord > 0.49) {
      return new String[] { "0", courseNameList.get(bestTitleMatchIdx)[1],
          courseNameList.get(bestTitleMatchIdx)[0] };
    }

    return new String[] { "-1", null, null };
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
    return pct;
  }

  /**
   * Set courseNameList.
   * 
   * @param courseNameList the courseNameList to set
   */
  public void setCourseNameList(List<String[]> courseNameList) {
    this.courseNameList = courseNameList;
  }

}