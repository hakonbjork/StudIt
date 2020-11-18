package studit.core.chatbot;

import static studit.core.Commons.contains;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.primitives.Floats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KeywordLinker {

  private List<KeywordLink> links;
  private BiMap<Integer, String> recognizedWords;
  private Map<String, List<Keyword[]>> commandIDs;
  private Map<String, Integer> precedences;
  private Map<String, String> dataMatches;

  /**
   * Initialize a new KeywordLinker instance to prepare for future command
   * macthing.
   * 
   * @param links List of known KeywordLinks.
   */
  public KeywordLinker(List<KeywordLink> links) {
    this.links = links;
    extractKeywords();
  }

  /**
   * Initializes recognizedWords and commandIDs.
   */
  private void extractKeywords() {
    recognizedWords = HashBiMap.create();
    commandIDs = new HashMap<>();
    precedences = new HashMap<>();
    dataMatches = new HashMap<>();

    Set<String> uniqueWords = new HashSet<>();

    // Populate the set of unique words, along with the precedences and dataMatches.
    for (KeywordLink link : links) {
      uniqueWords.addAll(link.getWords());
      precedences.put(link.getCommand(), link.getPrecedence());
      dataMatches.put(link.getCommand(), link.getDataMatch());
    }

    int id = 0;

    // Assign a unique ID to all known words.
    for (String word : uniqueWords) {
      recognizedWords.put(id, word);
      id += 1;
    }

    // Assign the unique word ID's to the commandIDs HashMap.
    for (KeywordLink link : links) {
      commandIDs.put(link.getCommand(), getKeywordArrayList(link.getKeywords()));
    }

  }

  /**
   * Create a list of Keyword-arrays that contain word IDs and their respective
   * match.
   * 
   * @param keywordLinks list of links obtained from the KeywordLink class
   * @return list containing all of our keyword matches
   */
  private List<Keyword[]> getKeywordArrayList(List<Map<String, Float>> keywordLinks) {

    List<Keyword[]> keywordArrayList = new ArrayList<>();

    // Iterate over all keyword links.
    for (Map<String, Float> link : keywordLinks) {
      Keyword[] keywords = new Keyword[link.size()];

      int i = 0;

      // Populate the keywordArrayList with new Keyword instances based on words and
      // their respective weight
      for (Map.Entry<String, Float> entry : link.entrySet()) {
        keywords[i] = new Keyword(entry.getKey(), entry.getValue());
        i += 1;
      }
      keywordArrayList.add(keywords);
    }

    return keywordArrayList;
  }

  /**
   * Get the HashMap of recognized words, where the keys are the ID's and the
   * values are the corresponding words.
   * 
   * @return HashMap of recognized words.
   */
  public Map<Integer, String> getRecognizedWords() {
    return recognizedWords;
  }

  /**
   * Get a list of the recognized words.
   * 
   * @return a list of recognized words.
   */
  public List<String> getRecognizedWordsList() {
    return new ArrayList<String>(recognizedWords.values());
  }

  /**
   * Iterates over recognized words in our vocabulary and finds the closest match.
   * 
   * @param word the word we want to match
   * @return id if match is over 65% confident, -1 if no match found
   */
  private int getKeywordID(String word) {

    int matchID = -1;
    float matchPct = 0.0f;
    int lastLen = 100;

    // Iterate over the recognized words.
    for (Map.Entry<Integer, String> entry : recognizedWords.entrySet()) {

      // Create a new stringbuffer for our potential word match.
      StringBuffer wordToCheck = new StringBuffer(entry.getValue());
      int differences = 0;
      int complements = 0;

      // Iterate over all chars in the word we match against.
      for (int i = 0; i < word.length(); i++) {
        char c = word.charAt(i);
        int matchIdx = wordToCheck.indexOf(String.valueOf(c));

        // If both words contain the same char, increment the complements and delete the
        // char from the wordToCheck stringbuffer.
        if (matchIdx >= 0) {
          complements++;
          wordToCheck.deleteCharAt(matchIdx);
        } else {
          differences++;
        }
      }

      // Calculate match percentage based on the matching chars along with word length
      // difference.
      float pct = complements / (float) (complements + differences);
      pct *= (word.length() - Math.abs(entry.getValue().length() - word.length())) / (float) word.length();

      // If the match is above 0.65 and better than the last one, update the ID,
      // percentage, and lastLen.
      if (pct >= 0.65f && pct >= matchPct && wordToCheck.length() <= lastLen) {
        matchID = entry.getKey();
        matchPct = pct;
        lastLen = entry.getValue().length();
      }
    }
    return matchID;
  }

  /**
   * Reads the user input and tries to match the input to the most probable
   * command.
   * 
   * @param words processed user input, where all special characters are removed
   *              and words are split by spaces.
   * @return Sorted list of Match objects, containing information about match
   *         percentage, precedence, and the command key identifier
   */
  public List<Match> matchCommand(String[] words) {

    Integer[] matchIDs = new Integer[words.length];

    // Find all the unique word ids of the words to match.
    for (int i = 0; i < words.length; i++) {
      matchIDs[i] = getKeywordID(words[i]);
    }

    List<Match> matches = new ArrayList<>();

    // Iterate over all commandIDs
    for (Map.Entry<String, List<Keyword[]>> entry : commandIDs.entrySet()) {

      float[] matchWeights = new float[entry.getValue().size()];
      int idx = 0;

      // Iterate over all the keyword[] of the spesific command we want to check
      for (Keyword[] keywords : entry.getValue()) {
        for (Keyword keyword : keywords) {
          // If there is a word match, update the matchWeights with the weight of the
          // words.
          if (contains(matchIDs, keyword.ID)) {
            matchWeights[idx] += keyword.weight;
          }
        }
        idx++;
      }

      // Add a new Match object to the list, containing information on matcPct,
      // precedence and command key.
      matches.add(new Match(entry.getKey(), Floats.max(matchWeights), precedences.get(entry.getKey()),
          dataMatches.get(entry.getKey()) == null ? "" : dataMatches.get(entry.getKey())));
    }

    // Sort the matches, first by precedence, then by match pct. Precedences are
    // sorted in ascending order, matchh percentages are sorted in descending order.
    matches.sort((left, right) -> {
      if (left.precedence > right.precedence) {
        return 1;
      } else if (left.precedence == right.precedence) {
        return left.match > right.match ? -1 : 1;
      } else {
        return -1;
      }

    });

    return matches;
  }

  class Keyword {
    public int ID;
    public float weight;

    /**
     * Intialize a new Keyword instance.
     * 
     * @param word   the keyword.
     * @param weight the weight the keyword is carrying. (A measure of how important
     *               the word is in a match.)
     */
    public Keyword(String word, float weight) {
      ID = recognizedWords.inverse().get(word);
      this.weight = weight;
    }

    @Override
    public String toString() {
      return "Keyword [ID=" + ID + ", weight=" + weight + "]";
    }
  }

}
