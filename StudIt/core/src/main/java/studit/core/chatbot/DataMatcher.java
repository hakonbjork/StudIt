package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;

public class DataMatcher {

  private List<String> wordsToExclude;
  private List<String[]> courseNameList;

  /**
   * Initialize a new DataMatcher object.
   * 
   * @param wordsToExclude list containing the words we want to exclude from data
   *                       matching.
   * @param courseNameList list containing all courses in the database.
   */
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

    // Exclude all words in wordsToExclude
    for (String word : userInput) {
      if (!wordsToExclude.contains(word)) {
        matchCandidates.add(word);
      }
    }

    // If we excluded all words, or input is empty return a no-match response.
    if (matchCandidates.isEmpty()) {
      return new String[] { "-1", null, null };
    }

    // Try to find a datamatch with the appropriate type of data.
    switch (dataMatch) {
      case "course":
        return matchCourse(matchCandidates);
      case "fagoversikt":
        return new String[] { "match" };
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
    int betsTitleLengthDiscrepancy = 0;

    // Iterate over all the courseNames.
    for (int i = 0; i < courseNameList.size(); i++) {
      String[] titleFragments = courseNameList.get(i)[1].split(" "); // Split course title by spaces.
      float titlePct = 0.0f;
      int titleMatches = 0;

      // Iterate over all possible match candidates
      for (String candidate : matchCandidates) {

        // Get a match percentage on the course fagkode
        float fagkodeMatch = matchWords(courseNameList.get(i)[0], candidate);

        // If the fagkode match is better than the previous best match, update index and
        // percentage.
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

        // If the title fragment match is better than the previous best match, update
        // index and
        // percentage.
        if (tmpMaxTitlePct > 0) {
          titlePct += tmpMaxTitlePct;
          titleMatches++;
        }
      }

      // If we found a new title match better than the previous, update the index,
      // percentage and how much the match differs in length of the actual title.
      if (titlePct > bestTitlePct) {
        bestTitleMatchIdx = i;
        bestTitlePct = titlePct;
        betsTitleLengthDiscrepancy = Math.abs(titleFragments.length - titleMatches);
      } else if (titlePct == bestTitlePct) {
        // If two titles have the same match, favor the one with the most similar title
        // length to the actual course title.
        int titleLengthDescrepancy = Math.abs(titleFragments.length - titleMatches);
        if (titleLengthDescrepancy <= betsTitleLengthDiscrepancy) {
          bestTitleMatchIdx = i;
          bestTitlePct = titlePct;
          betsTitleLengthDiscrepancy = titleLengthDescrepancy;
        }
      }
    }

    // If we found a match with confidence over 74%, return successfull / probable
    // fagkode match.
    if (bestFagkodePct > 0.74) {
      return new String[] { bestFagkodePct >= 1.0f ? "1" : "0", courseNameList.get(bestFagkodeMatchIdx)[0],
          courseNameList.get(bestFagkodeMatchIdx)[1] };
    }

    int titleSplitLength = courseNameList.get(bestTitleMatchIdx)[1].split(" ").length;
    float matchPctPerWord = bestTitlePct / (float) titleSplitLength;

    // If we found a match per word >= 1 return successful title match.
    if (matchPctPerWord >= 1.0f) {
      return new String[] { "1", courseNameList.get(bestTitleMatchIdx)[1], courseNameList.get(bestTitleMatchIdx)[0] };
    }

    // Return a probable title match if it exists.
    if (titleSplitLength == 1 && matchPctPerWord >= 0.7f || titleSplitLength > 1 && matchPctPerWord > 0.49) {
      return new String[] { "0", courseNameList.get(bestTitleMatchIdx)[1], courseNameList.get(bestTitleMatchIdx)[0] };
    }

    // No match found
    return new String[] { "-1", null, null };
  }

  /**
   * Matches two strings and returns the match percentage.
   * 
   * @param word1 one of the words.
   * @param word2 the other word.
   * @return float between 0 and 1, where 0 is no match, and 1 is 100% match.
   */
  private float matchWords(String word1, String word2) {
    int complements = 0;
    int differences = 0;

    // Intialize stringbuffer of the word to match.
    StringBuffer tmpWord = new StringBuffer(word2);

    // Iterate over all chars in.
    for (int i = 0; i < word1.length(); i++) {
      char c = word1.charAt(i);

      int matchIdx = tmpWord.indexOf(String.valueOf(c));
      // If the words both contains the same char, add 1 to the complements, and
      // remove the char from the stringbuffer.
      if (matchIdx >= 0) {
        complements++;
        tmpWord.deleteCharAt(matchIdx);
      } else {
        // Both words did not contain the same char.
        differences++;
      }
    }

    // Calculate a match percentage based on complements, differences and the different word lengths.
    float pct = complements / (float) (complements + differences);
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