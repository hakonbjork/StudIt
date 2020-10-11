package studit.core.chatbot.prompt;

import java.util.List;

@FunctionalInterface
public interface Func {
  public void execute(List<Object> args);
}
