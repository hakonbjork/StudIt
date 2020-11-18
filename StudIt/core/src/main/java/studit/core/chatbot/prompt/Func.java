package studit.core.chatbot.prompt;

import java.util.List;

/**
 * This functional interface represents functions we can pass from the core
 * module to the studit module. This is used whenever a new prompt is created in
 * the chatbot, and we want the different options to execute different commands.
 * The commands will be interpreted in the studit/ui/chatbot/Commands class,
 * where the arguments will be cast into the proper types based on the HashMap
 * key for the available functions. Note that the Command class does not perform
 * any type-safe casts, so if the wrong arguments are passed unpredictable
 * behaviour will occur. Always read the documentation in the Commands class
 * before using this.
 */
@FunctionalInterface
public interface Func {
  public void execute(List<Object> args);
}
