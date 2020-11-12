package studit.core.chatbot;

public class Match {
  public float match;
  public String command;
  public int precedence;
  public String dataMatch;

  /**
   * Initialize a new match instance.
   * 
   * @param command the command key
   * @param match match percentage
   * @param precedence precedence of the match
   * @param dataMatch match key, either empty or the type of data we want to look for.
   */
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
