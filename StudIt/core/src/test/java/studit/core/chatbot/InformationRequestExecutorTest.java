package studit.core.chatbot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static studit.core.chatbot.InformationRequestExecutor.executeCommand;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.json.StuditPersistence;

public class InformationRequestExecutorTest {

  private CourseList courseList;
  private ChatbotManager chatbotManager;

  @BeforeEach
  public void init() {
    try (Reader reader = new FileReader("../res/db/studitModel.json", StandardCharsets.UTF_8)) {
      courseList = new StuditPersistence().readStuditModel(reader).getCourseList();
      chatbotManager = new ChatbotManager(courseList.getCourseNameList());
    } catch (IOException e) {
      //System.out.println("Couldn't read default studitModel");
    }
  }

  @Test
  public void testRequestAnbefaltInfo() {
    Response response = chatbotManager.manageInput("Hva er anbefalt lesestoff i");
    executeCommand(response, courseList);
    assertFailedDataMatch(response,
        "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Hva er anbefalt lesestoff i TMA4140?'");

    Response response2 = chatbotManager.manageInput("Hva er anbefalt lesestoff i statstikk");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "anbefalt", "statistikk");

    Response response3 = chatbotManager.manageInput("Hva er anbefalt lesestoff i statistikk");
    executeCommand(response3, courseList);
    assertEquals(
        "Her har du våre anbefalinger i TMA4240: " + courseList.getCourseByFagkode("TMA4240").getAnbefaltLitteratur(),
        response3.getResponse());

    Response response4 = chatbotManager.manageInput("Hva er anbefalt lesestoff i TMA4240");
    executeCommand(response4, courseList);
    assertEquals(
        "Her har du våre anbefalinger i TMA4240: " + courseList.getCourseByFagkode("TMA4240").getAnbefaltLitteratur(),
        response4.getResponse());

  }

  @Test
  public void testRequestEksamensInfo() {
    Response response = chatbotManager.manageInput("jeg vil vite litt om eksamen i ");
    executeCommand(response, courseList);
    assertFailedDataMatch(response,
        "Jeg forstår ikke helt hvilken eksamen du lurer på, husk å spesifisere fag, f.eks 'Jeg vil vite mer om eksamen i Statistikk'");

    Response response2 = chatbotManager.manageInput("jeg vil vite om eksamen i tmt4105?");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "eksamen", "TMT4106");

    Response response4 = chatbotManager.manageInput("jeg vil vite om eksamen i kjemii?");
    executeCommand(response4, courseList);
    assertCloseDatamatch(response4, "eksamen", "kjemi");

    Response response3 = chatbotManager.manageInput("jeg vil vite om eksamen i tma4240?");
    executeCommand(response3, courseList);

    CourseItem course = courseList.getCourseByFagkode("TMA4240");
    String expected = "Her har du eksamensinformasjon for " + "TMA4240" + ": " + "eksamensdato: "
        + course.getEksamensdato() + ", vurderingsform: " + course.getVurderingsform() + ", tillatte hjelpemidler: "
        + course.getHjelpemidler();
    assertEquals(expected, response3.getResponse());

    Response response5 = chatbotManager.manageInput("jeg vil vite om eksamen i Statistikk?");
    executeCommand(response5, courseList);

    String expected2 = "Her har du eksamensinformasjon for " + "TMA4240" + ": " + "eksamensdato: "
        + course.getEksamensdato() + ", vurderingsform: " + course.getVurderingsform() + ", tillatte hjelpemidler: "
        + course.getHjelpemidler();
    assertEquals(expected2, response5.getResponse());

  }

  @Test
  public void testRequestEksamensDato() {
    Response response = chatbotManager.manageInput("når er eksamen i");
    executeCommand(response, courseList);
    assertFailedDataMatch(response, "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Når er eksamen i TMA4140?'");

    Response response2 = chatbotManager.manageInput("når er eksamen i TMA4130");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "eksamensdato", "TMA4140");

    Response response3 = chatbotManager.manageInput("Når er eksamen i TMA4140?");
    executeCommand(response3, courseList);
    assertEquals("Eksamen i " + "TMA4140" + " blir " + courseList.getCourseByFagkode("TMA4140").getEksamensdato(),
        response3.getResponse());

    Response response4 = chatbotManager.manageInput("Når er eksamen i Statistikk?");
    executeCommand(response4, courseList);
    assertEquals("Eksamen i " + "TMA4240" + " blir " + courseList.getCourseByFagkode("TMA4240").getEksamensdato(),
        response4.getResponse());
  }

  @Test
  public void testRequestFagoversikt() {
    Response response = chatbotManager.manageInput("jeg vil se fagoversikten");
    executeCommand(response, courseList);
    assertNotNull(response.getResponse());
  }

  protected void assertFailedDataMatch(Response response, String message) {
    assertEquals(message, response.getResponse());
    List<String[]> prompt = response.getPrompt();

    assertEquals("fagoversikt", prompt.get(0)[0]);
    assertEquals("fagoversikt", prompt.get(0)[1]);
    assertNull(response.getArgs1());
    assertNull(response.getArgs2());
  }

  @Test
  public void testRequestHjelpemidler() {
    Response response = chatbotManager.manageInput("hva er tillatte hjelpemidler i");
    executeCommand(response, courseList);
    assertFailedDataMatch(response,
        "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Hvilke hjelpemidler er tillatt i Statistikk?'");

    Response response2 = chatbotManager.manageInput("hva er tillatte hjelpemidler i TMA4130");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "hjelpemidler", "TMA4140");

    Response response3 = chatbotManager.manageInput("hva er tillatte hjelpemidler i TMA4140?");
    executeCommand(response3, courseList);
    assertEquals("På eksamen i " + "TMA4140" + " blir hjelpemidler: "
        + courseList.getCourseByFagkode("TMA4140").getHjelpemidler(), response3.getResponse());

    Response response4 = chatbotManager.manageInput("hva er tillate hjelpemidler i Statistikk?");
    executeCommand(response4, courseList);
    assertEquals("På eksamen i " + "TMA4140" + " blir hjelpemidler: "
        + courseList.getCourseByFagkode("TMA4140").getHjelpemidler(), response3.getResponse());
  }

  @Test
  public void testRequestTips() {
    Response response = chatbotManager.manageInput("har du noen tips i");
    executeCommand(response, courseList);
    assertFailedDataMatch(response,
        "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Har du noen tips i Fysikk?'");

    Response response2 = chatbotManager.manageInput("Har du noen tips i statstikk");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "tips", "statistikk");

    Response response3 = chatbotManager.manageInput("Har du noen tips i statistikk");
    executeCommand(response3, courseList);
    assertEquals("Her har du noen tips i " + "TMA4240" + ": " + courseList.getCourseByFagkode("TMA4240").getTips(),
        response3.getResponse());

    Response response4 = chatbotManager.manageInput("Har du noen tips i TMA4240");
    executeCommand(response4, courseList);
    assertEquals("Her har du noen tips i " + "TMA4240" + ": " + courseList.getCourseByFagkode("TMA4240").getTips(),
        response4.getResponse());

  }

  @Test
  public void testRequestPensum() {
    Response response = chatbotManager.manageInput("hva er pensum i");
    executeCommand(response, courseList);
    assertFailedDataMatch(response, "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Hva er pensum i TMA4140?'");

    Response response2 = chatbotManager.manageInput("Hva er pensum i statstikk");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "pensum", "statistikk");

    Response response3 = chatbotManager.manageInput("hva er pensum i statistikk");
    executeCommand(response3, courseList);
    assertEquals(
        "Dette er pensum i " + "TMA4240" + ": " + courseList.getCourseByFagkode("TMA4240").getAnbefaltLitteratur(),
        response3.getResponse());

    Response response4 = chatbotManager.manageInput("hva er pensum i TMA4240");
    executeCommand(response4, courseList);
    assertEquals(
        "Dette er pensum i " + "TMA4240" + ": " + courseList.getCourseByFagkode("TMA4240").getAnbefaltLitteratur(),
        response4.getResponse());

  }

  @Test
  public void testRequestVurderingsform() {
    Response response = chatbotManager.manageInput("blir det hjemmeksamen i");
    executeCommand(response, courseList);
    assertFailedDataMatch(response,
        "Jeg forstår ikke helt, husk å spesifisere fag, f.eks: 'Hvilken vurderingsform er det i TMA4140?'");

    Response response2 = chatbotManager.manageInput("blir det hjemmeksamen i statstikk");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "vurderingsform", "statistikk");

    Response response3 = chatbotManager.manageInput("blir det hjemmeksamen i statistikk");
    executeCommand(response3, courseList);
    assertEquals(
        "Vurderingsformen i " + "TMA4240" + " er:  " + courseList.getCourseByFagkode("TMA4240").getVurderingsform(),
        response3.getResponse());

    Response response4 = chatbotManager.manageInput("blir det hjemmeksamen i TMA4240");
    executeCommand(response4, courseList);
    assertEquals(
        "Vurderingsformen i " + "TMA4240" + " er:  " + courseList.getCourseByFagkode("TMA4240").getVurderingsform(),
        response4.getResponse());

  }

  @Test
  public void testRequestFaginfo() {
    Response response = chatbotManager.manageInput("jeg vil vite mer om");
    executeCommand(response, courseList);
    assertFailedDataMatch(response,
        "Jeg forstår ikke hvilken informasjon du etterspør, husk å spesifisere hvilket fag du vil vite mer om.");

    Response response2 = chatbotManager.manageInput("jeg vil vite mer om statstikk");
    executeCommand(response2, courseList);
    assertCloseDatamatch(response2, "faginfo", "statistikk");

    Response response3 = chatbotManager.manageInput("jeg vil vite mer om statistikk");
    executeCommand(response3, courseList);
    assertEquals(
        "Her har du litt informasjon om " + "TMA4240" + ": " + courseList.getCourseByFagkode("TMA4240").getInformasjon(),
        response3.getResponse());

    Response response4 = chatbotManager.manageInput("jeg vil vite mer om TMA4240");
    executeCommand(response4, courseList);
    assertEquals(
        "Her har du litt informasjon om " + "TMA4240" + ": " + courseList.getCourseByFagkode("TMA4240").getInformasjon(),
        response4.getResponse());

  }

  protected void assertCloseDatamatch(Response response, String funcName, String courseName) {
    assertEquals("Jeg er litt usiker på hvilket fag du mente, mente du '" + courseName + "'?", response.getResponse());

    List<String[]> prompt = response.getPrompt();

    assertEquals("ja", prompt.get(0)[0]);
    assertEquals(funcName, prompt.get(0)[1]);
    assertEquals("nei", prompt.get(1)[0]);
    assertEquals(funcName, prompt.get(1)[1]);
  }

}