package studit.core.chatbot;

import static studit.core.Commons.contains;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class KeywordLinker {

  private List<KeywordLink> links;
  private BiMap<Integer, String> recognizedWords;
  private Map<String, Keyword[]> commandIDs;
  private Map<String, Integer> precedences;

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

    Set<String> uniqueWords = new HashSet<>();

    for (KeywordLink link : links) {
      uniqueWords.addAll(link.getWords());
      precedences.put(link.getCommand(), link.getPrecedence());
    }

    int id = 0;

    for (String word : uniqueWords) {
      recognizedWords.put(id, word);
      id += 1;
    }

    for (KeywordLink link : links) {
      commandIDs.put(link.getCommand(), getIdArray(link.getKeywords()));
    }

  }

  /**
   * Finds the word id's in a string array.
   * 
   * @param weightedKeywords HashMap containing our words and weights
   * @return Keyword[] of weights and IDs
   */
  private Keyword[] getIdArray(Map<String, Float> weightedKeywords) {

    Keyword[] keywords = new Keyword[weightedKeywords.size()];

    int i = 0;
    for (Map.Entry<String, Float> entry : weightedKeywords.entrySet()) {

      keywords[i] = new Keyword(entry.getKey(), entry.getValue());
      i += 1;
    }

    return keywords;
  }

  public Map<Integer, String> getRecognizedWords() {
    return recognizedWords;
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

    for (Map.Entry<Integer, String> entry : recognizedWords.entrySet()) {
      int unions = 0;
      int complements = 0;
      int lastLen = 0;
      String wordToCheck = entry.getValue();

      for (int i = 0; i < wordToCheck.length(); i++) {
        char c = wordToCheck.charAt(i);

        if (word.indexOf(c) >= 0) {
          unions++;
        } else {
          complements++;
        }
      }

      float pct = unions / (float) (unions + complements);
      pct *= (word.length() - Math.abs(wordToCheck.length() - word.length())) / (float)  word.length();
      
      if (pct >= 0.65f && pct >= matchPct && wordToCheck.length() > lastLen) {
        matchID = entry.getKey();
        matchPct = pct;
        lastLen = wordToCheck.length();
      }
    }

    return matchID;

  }

  public List<Match> matchCommand(String[] words) {

    Integer[] matchIDs = new Integer[words.length];

    for (int i = 0; i < words.length; i++) {
      matchIDs[i] = getKeywordID(words[i]);
    }

    List<Match> matches = new ArrayList<>();

    for (Map.Entry<String, Keyword[]> entry : commandIDs.entrySet()) {
      float match = 0.0f;
      for (Keyword keyword : entry.getValue()) {
        if (contains(matchIDs, keyword.ID)) {
          match += keyword.weight;
        }
      }

      matches.add(new Match(entry.getKey(), match, precedences.get(entry.getKey())));

    }

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
