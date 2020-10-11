package studit.core.chatbot;

import java.util.List;

public class CommandManager {

  public CommandManager() {

  }

  public void executeCommand(String match, Response response) {
    String addition = "";

    switch (match) {
      case "avslutt":
        addition = "Er du sikker på at du vil avslutte samtalen?";
        response.prompt = List.of(new String[] {"ja", "exit"}, new String[] {"nei", "regret"});
        break;
      case "hade":
        addition = "Jeg håper jeg kunne være til hjelp! Takk for samtalen.";
        break;
      case "hils":
        addition = "Hei! ";
        break;
      case "nei":
        addition = "Neivel. ";
        break;
      case "hyggelig":
        addition = "Så bra da! Hva kan jeg hjelpe deg med?";
        break;
      case "uhyggelig":
        addition = "Det var leit å høre... Hva kan jeg hjelpe deg med?";
        break;
      case "høflig":
        addition = "Det går bra, takk, hvordan går det med deg?";
        break;
      default:
        addition = "Oops, internal error -> unrecognized command";
        break;
    }
    
    response.add(addition);
  }

}
