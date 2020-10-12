package studit.core.chatbot;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatbotManager {

  private KeywordLinker linker;
  private CommandManager cmg;

  public ChatbotManager() {
    writeDummyCommandsToDb();
    linker = new KeywordLinker(loadJson("keywordLinks.json"));
    cmg = new CommandManager();
  }

  /**
   * Manages the user entered input and executes commands accordingly.
   * 
   * @param input - the user input to process
   * @return chatbot response
   */
  public Response manageInput(String input) {
    // Splitting string by spaces, and removing all newline chars
    String[] command = input.replaceAll("[^a-zA-Z0-9 æøå]", "").toLowerCase().split(" ");

    Response response = new Response();
    List<Match> matches = linker.matchCommand(command);
    
    int nextPrecedence = 1;

    for (Match match : matches) {
      if (match.precedence == nextPrecedence) {
        if (match.match >= 1.0) {
          cmg.executeCommand(match.command, response);
        }
        nextPrecedence += 1;
      }
      // System.out.println(match);
    }

    if (response.prompt != null) {
      // Add this check to prevent spotbug, as functionallity is not fully implemented yet.
    }
    
    // System.out.println(response.prompt);

    if (response.response.length() == 0) {
      response.add(
          "Jeg beklager, men det forstod jeg ikke helt. Prøv å formulere setningen på en annen måte");
    }
    
    return response;

  }

  /**
   * Writes a list of dummy keyword connections to our json database.
   */
  private void writeDummyCommandsToDb() {

    List<KeywordLink> links = new ArrayList<>();
    
    links.add(new KeywordLink("avslutt", Map.of("avslutt", 1.0f), 1));
    links.add(new KeywordLink("hils",
        Map.of("hei", 1.0f, "hallo", 1.0f, "heisann", 1.0f, "hoi", 1.0f), 1));
    links.add(new KeywordLink("hade", Map.of("hade", 1.0f, "adjø", 1.0f, "vi", 0.2f, "snakkes",
        0.8f, "takk", 0.1f, "for", 0.1f, "hjelpen", 0.8f, "praten", 0.8f, "samtalen", 0.8f), 1));
    
    links.add(new KeywordLink("høflig", Map.of("hvordan", 0.3f, "går", 0.3f, "det", 0.4f), 2));
    links.add(new KeywordLink("høflig1", Map.of("hva", 0.5f, "skjer", 0.5f), 2));
    
    links.add(new KeywordLink("hyggelig", Map.of("det", 0.2f, "går", 0.2f, "bra", 0.6f, "greit", 0.6f, "strålende", 0.6f, "fantastisk", 0.6f, "ok", 0.6f), 1));
    links.add(new KeywordLink("uhyggelig", Map.of("det", 0.2f, "går", 0.2f, "dårlig", 0.6f, "ikke", 0.4f, "så", 0.05f, "bra", 0.05f), 1));

    
    links.add(new KeywordLink("nei", Map.of("nei", 1.0f, "nope", 1.0f, "niks", 1.0f), 1));


    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(
          Paths.get("../core/src/main/resources/studit/db/keywordLinks.json").toFile(), links);
    } catch (IOException e) {
      System.out.println("Error occured while printing dummy json to file");
      e.printStackTrace();
    }

  }

  /**
   * Reads Json file from given filename and returns the list of Keyword Links.
   * 
   * @param filename - Filename under resources/studit/db. E.g "test.json"
   * @return ArrayList containing our keyword links
   */
  private List<KeywordLink> loadJson(String filename) {
    String path = "../core/src/main/resources/studit/db/" + filename;

    ObjectMapper mapper = new ObjectMapper();
    try {
      List<KeywordLink> links =
          mapper.readValue(Paths.get(path).toFile(), new TypeReference<List<KeywordLink>>() {});
      return links;
    } catch (IOException e) {
      System.out.println("Error occured while reading json '" + filename + "'.");
      e.printStackTrace();
    }
    return null;
  }

}
