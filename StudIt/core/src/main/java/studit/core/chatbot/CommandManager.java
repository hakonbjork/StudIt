package studit.core.chatbot;

public class CommandManager {

  public CommandManager() {

  }

  public String executeCommand(String match) {
    String response = "";

    switch (match) {
      case "hils":
        response = "Hei! ";
        break;
      case "hade":
        response = "Jeg håper jeg kunne være til hjelp! Takk for samtalen.";
        break;
      case "høflig":
        response = "Det går bra, takk, hvordan går det med deg?";
        break;
      default:
        response = "Oops, internal error -> unrecognized command";
        break;
    }
    
    return response;
  }

}
