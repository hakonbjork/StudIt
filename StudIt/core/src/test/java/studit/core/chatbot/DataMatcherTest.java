package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
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
    assertEquals("1", result1[0]);
    assertEquals("tma4140", result1[1]);
    assertEquals("diskret matematikk 1", result1[2]);

    String[] result2 = dataMatcher.findDataMatch(new String[] { "hei", "jeg", "vil", "vite", "mer", "om", "tma4150" },
        "course");

    assertEquals("0", result2[0]);
    assertEquals("tma4140", result2[1]);
    assertEquals("diskret matematikk 1", result2[2]);

    String[] result3 = dataMatcher
        .findDataMatch(new String[] { "kan", "du", "fortelle", "om", "diskret", "matematikk", "1" }, "course");

    assertEquals("1", result3[0]);
    assertEquals("diskret matematikk 1", result3[1]);
    assertEquals("tma4140", result3[2]);

    String[] result4 = dataMatcher.findDataMatch(new String[] { "kan", "du", "fortelle", "om" }, "course");
    assertEquals("-1", result4[0]);
    assertNull(result4[1]);
    assertNull(result4[2]);

    String[] result5 = dataMatcher.findDataMatch(new String[] { "fortell", "meg", "om", "matematikk", "1" }, "course");

    assertEquals("0", result5[0]);
    assertEquals("diskret matematikk 1", result5[1]);
    assertEquals("tma4140", result5[2]);

    assertEquals("-1", dataMatcher.findDataMatch(new String[] { "kan", "du", "fortelle", "om" }, "foo")[0]);
    assertEquals("-1", dataMatcher.findDataMatch(new String[] {}, "foo")[0]);

    String[] result6 = dataMatcher
        .findDataMatch(new String[] { "fortell", "meg", "om", "informasjonsteknologi", "avansert", "gunnar" }, "course");

    assertEquals("0", result6[0]);
    assertEquals("tdt4120", result6[2]);

    dataMatcher.setCourseNameList(List.of(new String[] { "tma3240", "statistikk" }, new String[] { "FOO", "FOO" }));

    String[] result7 = dataMatcher.findDataMatch(new String[] { "fortell", "meg", "om", "statstikk", "gunnar" },
        "course");
    assertEquals("0", result7[0]);
    assertEquals("statistikk", result7[1]);
    assertEquals("tma3240", result7[2]);

  }

}