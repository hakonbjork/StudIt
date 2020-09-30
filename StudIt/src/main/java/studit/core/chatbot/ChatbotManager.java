package studit.core.chatbot;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import studit.core.chatbot.KeyboardLinker.Match;

public class ChatbotManager {
	
	private KeyboardLinker linker;
	
	public ChatbotManager() {
		writeDummyCommandsToDb();
		linker = new KeyboardLinker(loadJson("keywordLinks.json"));
		System.out.println(linker.getRecognizedWords());
	}
	
	/**
	 * Manages the user entered input and executes commands accordingly.
	 * @param input - the user input to process
	 * @return chatbot response
	 */
	public String manageInput(String input) {
		// Splitting string by spaces, and removing all newline chars
		String[] command = input.replaceAll("\n", "").toLowerCase().split(" ");
		
		
		String response = "";
		
		for (String word : command) {
			if (word.contains("hei")) {
				response += "Hei. ";
				break;
			}
		}
		
		List<Match> matches = linker.matchCommand(command);
		for (Match match : matches) {
			System.out.println(match);
		}
		
		
		if (response.equals("")) {
			response += "Jeg beklager, men det forstod jeg ikke helt. Kanskje du mente...?";
		}
		return response;
		
	}
	
	/**
	 * Writes a list of dummy keyword connections to our json database
	 */
	private void writeDummyCommandsToDb() {
		
		List<KeywordLink> links = new ArrayList<>();
		
		
		links.add(new KeywordLink("greeting",  Map.of(
				"hei", 10.0f, "hallo", 10.0f, "heisann", 10.0f, "hoi", 5.0f), 1));
		links.add(new KeywordLink("foo1",  Map.of(
				"more", 0.8f, "random", 0.4f, "stuff", 0.6f), 2));
		links.add(new KeywordLink("foo2",  Map.of(
				"you", 0.8f, "get", 0.4f, "the", 0.6f, "picture", 0.1f), 2));
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(Paths.get("src/main/resources/studit/db/keywordLinks.json").toFile(), links);
		} catch (IOException e) {
			System.out.println("Error occured while printing dummy json to file");
			e.printStackTrace();
		}

	}
	
	/**
	 * Reads Json file on the format List<KeywordLink> and returns the list
	 * @param filename - Filename under resources/studit/db. E.g "test.json"
	 * @return ArrayList containing our keyword links
	 */
	private List<KeywordLink> loadJson(String filename) {
		String path = "src/main/resources/studit/db/" + filename;
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			List<KeywordLink> links = mapper.readValue(Paths.get(path).toFile(), new TypeReference<List<KeywordLink>>(){});
			return links;
		} catch (IOException e) {
			System.out.println("Error occured while reading json '" + filename + "'.");
			e.printStackTrace();
		}
		return null;
	}

}
