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
  private DataMatcher dataMatcher;

  public ChatbotManager(List<String[]> courseNameList) {
    writeDefaultCommandsToDb();
    linker = new KeywordLinker(loadJson("keywordLinks.json"));
    dataMatcher = new DataMatcher(linker.getRecognizedWordsList(), courseNameList);
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
    System.out.println(matches);

    int nextPrecedence = 1;

    for (Match match : matches) {
      if (match.precedence == nextPrecedence) {
        if (match.match >= 1.0) {
          cmg.executeCommand(match.command, response);
          if (!match.dataMatch.isEmpty()) {
            response.handleMatchResult(dataMatcher.findDataMatch(command, match.dataMatch));
            response.setFuncKey(match.command);
          }
        }
        nextPrecedence += 1;
      }
      // System.out.println(match);
    }

    if (response.getResponse().length() == 0 && !response.funcCall()) {
      response.add("Jeg beklager, men det forstod jeg ikke helt. Prøv å formulere setningen på en annen måte");
    }

    return response;

  }

  /**
   * Writes a list of dummy keyword connections to our json database.
   */
  private void writeDefaultCommandsToDb() {

    List<KeywordLink> links = new ArrayList<>();

    links.add(new KeywordLink("avslutt", null, 1, List.of(Map.of("avslutt", 1.0f),
        Map.of("kan", 0.2f, "du", 0.2f, "lukke", 0.2f, "lukk", 0.6f, "chatboten", 0.4f))));

    links.add(new KeywordLink("takk", null, 1, List.of(Map.of("takk", 1.0f))));

    links.add(
        new KeywordLink("hils", null, 1, List.of(Map.of("hei", 1.0f, "hallo", 1.0f, "heisann", 1.0f, "hoi", 1.0f))));

    links.add(new KeywordLink("hade", null, 1, List.of(Map.of("hade", 1.0f, "adjø", 1.0f, "vi", 0.2f, "snakkes", 0.8f,
        "takk", 0.1f, "for", 0.1f, "hjelpen", 0.8f, "praten", 0.8f, "samtalen", 0.8f))));

    links.add(new KeywordLink("høflig", null, 2,
        List.of(Map.of("hvordan", 0.3f, "går", 0.3f, "det", 0.4f), Map.of("hva", 0.5f, "skjer", 0.5f))));

    links.add(new KeywordLink("hyggelig", null, 1, List.of(Map.of("det", 0.2f, "går", 0.2f, "bra", 0.6f, "greit", 0.6f,
        "strålende", 0.6f, "fantastisk", 0.6f, "ok", 0.6f))));

    links.add(new KeywordLink("uhyggelig", null, 1,
        List.of(Map.of("det", 0.2f, "går", 0.2f, "dårlig", 0.6f, "ikke", 0.4f, "så", 0.05f, "bra", 0.05f))));

    links.add(new KeywordLink("uhyggelig", null, 1, List.of(Map.of("nei", 1.0f, "nope", 1.0f, "niks", 1.0f))));

    links.add(new KeywordLink("faginfo", "course", 2,
        List.of(Map.of("jeg", 0.1f, "vil", 0.1f, "vite", 0.4f, "mer", 0.3f, "om", 0.3f),
            Map.of("kan", 0.1f, "du", 0.1f, "fortelle", 0.6f, "meg", 0.2f, "om", 0.2f))));

    ObjectMapper mapper = new ObjectMapper();
    try {
      mapper.writeValue(Paths.get("../core/src/main/resources/studit/db/keywordLinks.json").toFile(), links);
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
      List<KeywordLink> links = mapper.readValue(Paths.get(path).toFile(), new TypeReference<List<KeywordLink>>() {
      });
      return links;
    } catch (IOException e) {
      System.out.println("Error occured while reading json '" + filename + "'.");
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Get the active dataMatcher object.
   * 
   * @return the dataMatcher
   */
  public DataMatcher getDataMatcher() {
    return dataMatcher;
  }

}
