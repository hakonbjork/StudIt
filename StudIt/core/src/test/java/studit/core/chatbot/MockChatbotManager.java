package studit.core.chatbot;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import studit.json.StuditPersistence;

public class MockChatbotManager {

  /**
   * Creates a mock ChatbotManager by loading the studitModel found in
   * res/db/defaultModel.
   * 
   * @return ChatbotManager object
   */
  public static ChatbotManager getMockChatbotManager() {
    try (Reader reader = new FileReader("../res/db//default/defaultModel.json", StandardCharsets.UTF_8)) {
      ChatbotManager chatbotManager = new ChatbotManager(
          new StuditPersistence().readStuditModel(reader).getCourseList().getCourseNameList());
      return chatbotManager;
    } catch (IOException e) {
      //System.out.println("Couldn't read default studitModel");
    }
    return null;
  }

}