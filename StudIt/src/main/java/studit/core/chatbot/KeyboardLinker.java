package studit.core.chatbot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeyboardLinker {
	
	private Map<String, String[]> keywords;
	private Map<Integer, String> recognizedWords;
	
	public KeyboardLinker(Map<String, String[]> keywords) {
		this.keywords = keywords;
		extractKeywords();
		getKeywordId("randum");
		getKeywordId("mure");
		getKeywordId("hallelujah");
	}
	
	/**
	 * Initializes recognizedWords list and adds to it all the words from the keywords hashmap.
	 */
	private void extractKeywords() {
		recognizedWords = new HashMap<>();
		// This will be our word ID
		int i = 0;
		for (String[] command : keywords.values()) {
			for (String word : command) {
				recognizedWords.put(i, word);
				i += 1;
			}
		}
	}
	
	public Map<Integer, String> getRecognizedWords() {
		return recognizedWords;
	}
	
	private int getKeywordId(String word) {
		
		int matchID = -1;
		float matchPct = 0.0f;
		
		for (Integer key : recognizedWords.keySet()) {
			int unions = 0, complements = 0;
			for (int i = 0; i < word.length(); i++) {
			    char c = word.charAt(i);
			    
			    if (recognizedWords.get(key).indexOf(c) >= 0) {
			    	unions++;
			    } else {
			    	complements++;
			    }
			}
			
			float pct = (float) (unions-complements) / (unions + complements);
			System.out.println("Word: " + word + " matching:" + recognizedWords.get(key) + " pct: " + pct);
			if (pct >= 0.5f && pct > matchPct) {
				matchID = key;
				matchPct = pct;
			}
		}
		
		return matchID;

		
	}

}
