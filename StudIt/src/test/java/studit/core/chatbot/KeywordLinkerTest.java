package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class KeywordLinkerTest {


  private KeywordLinker linker;
  private List<KeywordLink> dummyLinks;

  @BeforeEach
  public void init() {

    dummyLinks = new ArrayList<>();

    dummyLinks.add(new KeywordLink("hils",
        Map.of("hei", 1.0f, "hallo", 1.0f, "heisann", 1.0f, "hoi", 1.0f), 1));
    dummyLinks.add(
        new KeywordLink("hade", Map.of("hade", 0.8f, "adjø", 0.4f, "bye", 0.6f, "adios", 0.1f), 2));

    linker = new KeywordLinker(dummyLinks);
  }

  @Test
  public void testGetRecognizedWords() {
    fail("Not yet implemented");
  }

  @Test
  public void testMatchCommand() {
    fail("Not yet implemented");
  }

}
