package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class ResponseTest {

  @Test
  public void testGettersAndSetters() {
    Response response = new Response();
    response.setResponse("foo");
    assertEquals("foo", response.getResponse());

    List<String[]> prompt = List.of(new String[] { "foo1", "foo2" }, new String[] { "foo3", "foo4" });
    List<Object> o1 = List.of(12, "a");
    List<Object> o2 = List.of(5, "b");

    response.setPrompt(prompt, o1, o2);

    List<String[]> retrievedPrompt = response.getPrompt();
    for (int i = 0; i < prompt.size(); i++) {
      assertEquals(prompt.get(i)[0], retrievedPrompt.get(i)[0]);
      assertEquals(prompt.get(i)[1], retrievedPrompt.get(i)[1]);
    }

    List<Object> retrievedO1 = response.getArgs1();
    List<Object> retrievedO2 = response.getArgs2();

    for (int i = 0; i < o1.size(); i++) {
      assertEquals(o1.get(i), retrievedO1.get(i));
      assertEquals(o2.get(i), retrievedO2.get(i));
    }
  }

  @Test
  public void testHandleArguments() {
    Response response = new Response();
    response.handleMatchResult(new String[] { "0", "foo1", "foo2" });
    assertTrue(response.funcCall());
    List<String> arguments = response.getArguments();

    assertEquals(arguments.get(0), "0");
    assertEquals(arguments.get(1), "foo1");
    assertEquals(arguments.get(2), "foo2");

  }

}