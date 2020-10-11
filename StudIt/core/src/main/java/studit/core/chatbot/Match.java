package studit.core.chatbot;

public class Match {
  public float match;
  public String command;
  public int precedence;

  public Match(String command, float match, int precedence) {
    // super();
    this.match = match;
    this.command = command.replaceAll("[0-9]", "");
    this.precedence = precedence;
  }

  @Override
  public String toString() {
    return "Match [match=" + match + ", command=" + command + ", precedence=" + precedence + "]";
  }

}
