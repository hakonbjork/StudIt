package studit.ui.chatbot;

import java.util.HashMap;
import java.util.List;
import studit.core.chatbot.prompt.Func;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;
import studit.ui.remote.ApiCallException;
import studit.ui.remote.RemoteStuditModelAccess;

public class Commands {
  private final static String connectionFailedMsg = "Kunne ikke etablere tilkobling til serveren";

  private HashMap<String, Func> commands;
  private ChatbotController chatbotController;
  private RemoteStuditModelAccess remoteAccess;

  /**
   * Initialize the Commands class.
   * 
   * @param chatbotController active ChatbotController
   * @param remoteAccess      active StuditModelAccess
   */
  public Commands(ChatbotController chatbotController, RemoteStuditModelAccess remoteAccess) {
    this.chatbotController = chatbotController;
    this.remoteAccess = remoteAccess;
    commands = new HashMap<>();
    initializeCommands();
  }

  /**
   * Initialize our hashmap of commands with the appropriate functions.
   */
  private void initializeCommands() {
    commands.put("anbefalt", (args) -> anbefalt(args));
    commands.put("eksamen", (args) -> eksamen(args));
    commands.put("eksamensdato", (args) -> eksamensdato(args));
    commands.put("exit", (args) -> exit());
    commands.put("faginfo", (args) -> faginfo(args));
    commands.put("fagoversikt", (args) -> fagoversikt());
    commands.put("hjelpemidler", (args) -> hjelpemidler(args));
    commands.put("tips", (args) -> tips(args));
    commands.put("pensum", (args) -> pensum(args));
    commands.put("vurderingsform", (args) -> vurderingsform(args));
  }

  /**
   * Get the command hashmap.
   * 
   * @return the command hashmap.
   */
  public HashMap<String, Func> getCommands() {
    return commands;
  }

  /**
   * This is called whenever user clicks the "nei" option in the prompt and we
   * guessed the wrong course. Pring generic message to the user.
   */
  private void courseGuessDeclined() {
    chatbotController.list_chat.getItems()
        .add(new Message("Den er grei du, da eksisterer sannsynligvis ikke faget du har etterspurt.", "chatbot"));
  }

  // ------------------------------------COMMANDS----------------------------------

  /**
   * Exit the chatbot.
   */
  private void exit() {
    chatbotController.exitChatbot(null);
  }

  /**
   * Print reccomended litterature of the course specified in the args.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void anbefalt(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message("Her har du våre anbefalinger i " + fagkode + ": "
            + remoteAccess.getCourseByFagkode(fagkode).getAnbefaltLitteratur(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print information on the exam of the course specified in the args.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void eksamen(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        CourseItem course = remoteAccess.getCourseByFagkode(fagkode);
        chatbotController.list_chat.getItems()
            .add(new Message("Her har du eksamensinformasjon for " + fagkode + ": " + "eksamensdato: "
                + course.getEksamensdato() + ", vurderingsform: " + course.getVurderingsform()
                + ", tillatte hjelpemidler: " + course.getHjelpemidler(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print the exam date of the specified course.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void eksamensdato(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message(
            "Eksamen i " + fagkode + " blir " + remoteAccess.getCourseByFagkode(fagkode).getEksamensdato(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print information about the course specified in the args.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void faginfo(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message("Her kommer litt informasjon om " + fagkode + ": "
            + remoteAccess.getCourseByFagkode(fagkode).getInformasjon(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print the allowed aids on the exam by the specified course.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void hjelpemidler(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message("På eksamen i " + fagkode + " blir hjelpemidler: "
            + remoteAccess.getCourseByFagkode(fagkode).getHjelpemidler(), "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print the allowed aids on the exam by the specified course.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void tips(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems()
            .add(new Message(
                "Her har du noen tips i " + fagkode + ": " + remoteAccess.getCourseByFagkode(fagkode).getTips(),
                "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print the curricilum of the specified course.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void pensum(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems()
            .add(new Message(
                "Dette er pensum i " + fagkode + ": " + remoteAccess.getCourseByFagkode(fagkode).getPensumlitteratur(),
                "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print information on grading on the exam by the specified course.
   * 
   * @param args list of arguments to pass to the method. Type must be either
   *             String or null.
   */
  private void vurderingsform(List<Object> args) {
    if (!args.isEmpty()) {
      String fagkode = ((String) args.get(0)).toUpperCase();
      try {
        chatbotController.list_chat.getItems().add(new Message(
            "Vurderingsformen i " + fagkode + " er:  " + remoteAccess.getCourseByFagkode(fagkode).getVurderingsform(),
            "chatbot"));
      } catch (ApiCallException e) {
        chatbotController.list_chat.getItems()
            .add(new Message(connectionFailedMsg, "chatbot"));
      }
    } else {
      courseGuessDeclined();
    }
  }

  /**
   * Print a list of the currently available courses in the database.
   */
  private void fagoversikt() {
    try {
      CourseList courseList = remoteAccess.getCourseList();
      StringBuffer buffer = new StringBuffer("%S Fagoversikt: %S ");
      for (CourseItem courseItem : courseList) {
        String str = courseItem.getFagkode() + " - " + courseItem.getFagnavn();
        buffer.append(str);
        buffer.append(" %S ");
      }
      chatbotController.list_chat.getItems().add(new Message(buffer.toString(), "chatbot"));
    } catch (ApiCallException e) {
      chatbotController.list_chat.getItems().add(new Message(connectionFailedMsg, "chatbot"));
    }
  }
}
