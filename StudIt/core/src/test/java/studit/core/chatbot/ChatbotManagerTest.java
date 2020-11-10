package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.StuditModel;
import studit.json.StuditPersistence;

public class ChatbotManagerTest {

  private ChatbotManager chatbotManager;
  private StuditModel defaultModel;

  @BeforeEach
  public void init() {
    try (Reader reader = new FileReader("../res/db/studitModel.json", StandardCharsets.UTF_8)) {
      defaultModel = new StuditPersistence().readStuditModel(reader);
    } catch (IOException e) {
      System.out.println("Couldn't read default studitModel");
    }

    chatbotManager = new ChatbotManager(defaultModel.getCourseList().getCourseNameList());

  }

  @Test
  public void testManageInput() {
    assertEquals("Hei! ", chatbotManager.manageInput("Hei!").getResponse());

    Response noMatchResponse = chatbotManager.manageInput("Dette er bare vissvass");
    assertEquals(defaultResponse, noMatchResponse.getResponse());
    assertFalse(noMatchResponse.funcCall());

    Response anbefaltResponse = chatbotManager.manageInput("Hva er anbefalt lesestoff i Statistikk?");
    assertEquals("", anbefaltResponse.getResponse());
    assertTrue(anbefaltResponse.funcCall());
    assertEquals("anbefalt", anbefaltResponse.getFuncKey());

    Response eksamensResponse = chatbotManager.manageInput("Når er eksamen i Statistik?");
    assertEquals("", eksamensResponse.getResponse());
    assertEquals("eksamensdato", eksamensResponse.getFuncKey());

    Response avsluttResponse = chatbotManager.manageInput("jeg vil avslutte samtalen.");
    assertEquals(exitResponse, avsluttResponse.getResponse());
    assertNotNull(avsluttResponse.getPrompt());

    Response fagoversiktResponse = chatbotManager.manageInput("kan jeg få fagoversikten?");
    assertNotEquals(defaultResponse, fagoversiktResponse.getResponse());
    assertEquals("fagoversikt", fagoversiktResponse.getFuncKey());

    assertEquals(goodbyeResponse, chatbotManager.manageInput("hade bra Gunnar!").getResponse());
    assertNotEquals(defaultResponse, chatbotManager.manageInput("jeg trenger hjelp!").getResponse());
    assertEquals(politeResponse, chatbotManager.manageInput("hvordan går det, Gunnar?").getResponse());
    assertEquals(declineResponse, chatbotManager.manageInput("nei!").getResponse());
    assertEquals(sadResponse, chatbotManager.manageInput("Det går dårlig ass").getResponse());
    assertEquals(welcomeResponse, chatbotManager.manageInput("takk skal du ha!").getResponse());

    assertEquals(missingResponse + "statistikk'", chatbotManager.manageInput("hummus statistikk").getResponse());
    assertEquals(missingResponse + "tma4140'", chatbotManager.manageInput("bjørnen sover i TMA4130").getResponse());

  }

  /**
   * ------------------------------- Responses----------------------------
   * 
   */

  private String defaultResponse = "Jeg beklager, men det forstod jeg ikke helt. Prøv å formulere setningen på en annen måte. Skriv 'jeg trenger hjelp' hvis du står fast";
  private String missingResponse = "Vennligst vær litt mer spesifikk, f.eks: 'Jeg vil vite mer om ";
  private String exitResponse = "Er du sikker på at du vil avslutte samtalen?";
  private String goodbyeResponse = "Jeg håper jeg kunne være til hjelp! Takk for samtalen.";
  private String politeResponse = "Det går bra, takk, hvordan går det med deg?";
  private String declineResponse = "Neivel. ";
  private String sadResponse = "Det var leit å høre... Hva kan jeg hjelpe deg med?";
  private String welcomeResponse = "Bare hyggelig :)";
}
