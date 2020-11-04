package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DataMatcherTest {

  private DataMatcher dataMatcher;

  @BeforeEach
  public void setUp() {
    dataMatcher = MockChatbotManager.getMockChatbotManager().getDataMatcher();
  }

  @Test
  public void testFindDataMatch() {
    String[] result1 = dataMatcher.findDataMatch(new String[] { "hei", "jeg", "vil", "vite", "mer", "om", "tma4140" },
        "course");

    assertEquals("tma4140", result1[0]);

    String[] result2 = dataMatcher.findDataMatch(new String[] { "hei", "jeg", "vil", "vite", "mer", "om", "tme4150" },
        "course");

    assertNull(result2[0]);
    assertEquals("tma4140", result2[1]);

    String[] result3 = dataMatcher.findDataMatch(new String[] { "kan", "du", "fortelle", "om", "diskret matematik" },
        "course");

    assertNull(result3[0]);
    assertEquals("diskret matematikk 1", result3[1]);

    String[] result4 = dataMatcher.findDataMatch(new String[] { "kan", "du", "fortelle", "om" }, "course");
    assertNull(result4[0]);
    assertNull(result4[1]);

    assertNull(dataMatcher.findDataMatch(new String[] { "kan", "du", "fortelle", "om" }, "foo"));
    assertNull(dataMatcher.findDataMatch(new String[] {}, "foo"));

  }

}