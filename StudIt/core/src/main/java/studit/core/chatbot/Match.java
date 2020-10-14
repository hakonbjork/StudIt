package studit.core.chatbot;

public class Match {
  public float match;
  public String command;
  public int precedence;
  public String dataMatch;

  public Match(String command, float match, int precedence, String dataMatch) {
    this.match = match;
    this.command = command;
    this.precedence = precedence;
    this.dataMatch = dataMatch;
  }

  @Override
  public String toString() {
    return "Match [match=" + match + ", command=" + command + ", precedence=" + precedence
        + ", dataMatch=" + dataMatch + "]";
  }

}
