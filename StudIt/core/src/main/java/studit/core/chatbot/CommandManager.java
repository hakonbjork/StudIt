package studit.core.chatbot;

import java.util.List;

public class CommandManager {

  public CommandManager() {

  }

  /**
   * Set the appropriate response by the specified match.
   * 
   * @param match    command key found in keywordlinks.json
   * @param response current response object we wish to further build on.
   */
  public void executeCommand(String match, Response response) {
    String addition = "";

    switch (match) {
      case "avslutt":
        addition = "Er du sikker på at du vil avslutte samtalen?";
        response.setPrompt(List.of(new String[] { "ja", "exit" }, new String[] { "nei", "regret" }), null, null);
        break;
      case "hade":
        addition = "Jeg håper jeg kunne være til hjelp! Takk for samtalen.";
        break;
      case "hjelp":
        addition = getHelpInfo();
        break;
      case "hils":
        addition = "Hei! ";
        break;
      case "hyggelig":
        addition = "Så bra da! Hva kan jeg hjelpe deg med?";
        break;
      case "høflig":
        addition = "Det går bra, takk, hvordan går det med deg?";
        break;
      case "nei":
        addition = "Neivel. ";
        break;
      case "uhyggelig":
        addition = "Det var leit å høre... Hva kan jeg hjelpe deg med?";
        break;
      case "takk":
        addition = "Bare hyggelig :)";
        break;
      default:
        addition = "";
        break;
    }

    response.add(addition);
  }

  /**
   * Print to the user some examples of what the chatboc can do.
   * 
   * @return String to print to the user.
   */
  private String getHelpInfo() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(
        "Jeg er ganske fleksibel, og du trenger ikke å tenke på skrivefeil eller tegnsetting. Her er noen eksempler på spørsmål du kan stille: %S ");
    buffer.append("Kan du fortelle meg litt om Statistikk? %S ");
    buffer.append("Blir det hjemmeksamen i TMA4140? %S ");
    buffer.append("I Diskret Matematikk, hvilke bøker anbefaler du? %S ");
    buffer.append("Når er eksamen i Fysikk? %S ");
    buffer.append("Har du noen tips i IT1901? %S ");
    buffer.append("Jeg vil avslutte samtalen. %S ");
    buffer.append("Kan jeg se fagoversikten? %S ");
    buffer.append("Hvilke hjelpemidler er tillat på eksamen i Mekanikk 1? %S ");

    return buffer.toString();
  }

}
