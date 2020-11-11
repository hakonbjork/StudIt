package studit.core.chatbot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import studit.core.mainpage.CourseItem;
import studit.core.mainpage.CourseList;

public class InformationRequestExecutor {

  /**
   * Update the response object with the appropriate repsonse given the current
   * matching data, and create a new user prompt if applicable.
   * 
   * @param response   active Response object
   * @param courseList CourseList obtained from API
   */
  public static void executeCommand(Response response, CourseList courseList) {

    List<String> args = response.getArguments();

    switch (response.getFuncKey()) {
      case "faginfo":
        requestFaginfo(response, courseList, args);
        break;
      case "anbefalt":
        requestAnbefaltInfo(response, courseList, args);
        break;
      case "eksamen":
        requestEksamensInfo(response, courseList, args);
        break;
      case "eksamensdato":
        requestEksamensDato(response, courseList, args);
        break;
      case "hjelpemidler":
        requestHjelpemidler(response, courseList, args);
        break;
      case "fagoversikt":
        requestFagoversikt(response, courseList, args);
        break;
      case "tips":
        requestTips(response, courseList, args);
        break;
      case "pensum":
        requestPensum(response, courseList, args);
        break;
      case "vurderingsform":
        requestVurderingsform(response, courseList, args);
        break;
      default:
    }

  }

  /**
   * Manage request where user wants reccomended information in a given subject.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestAnbefaltInfo(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response,
            "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Hva er anbefalt lesestoff i TMA4140?'");
        break;
      case "0":
        closeFagMatch(response, args, "anbefalt");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add("Her har du våre anbefalinger i " + fagkode + ": "
            + courseList.getCourseByFagkode(fagkode).getAnbefaltLitteratur());
        break;
      default:
        break;
    }
  }

  /**
   * Manage request where user wants information about the exams.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestEksamensInfo(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response,
            "Jeg forstår ikke helt hvilken eksamen du lurer på, husk å spesifisere fag, f.eks 'Jeg vil vite mer om eksamen i Statistikk'");
        break;
      case "0":
        closeFagMatch(response, args, "eksamen");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        CourseItem course = courseList.getCourseByFagkode(fagkode);
        response.add("Her har du eksamensinformasjon for " + fagkode + ": " + "eksamensdato: "
            + course.getEksamensdato() + ", vurderingsform: " + course.getVurderingsform() + ", tillatte hjelpemidler: "
            + course.getHjelpemidler());
        break;
      default:
        break;
    }
  }

  /**
   * Manage request where user wants to know the exam date.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestEksamensDato(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response, "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Når er eksamen i TMA4140?'");
        break;
      case "0":
        closeFagMatch(response, args, "eksamensdato");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add("Eksamen i " + fagkode + " blir " + courseList.getCourseByFagkode(fagkode).getEksamensdato());
        break;
      default:
        break;
    }
  }

  /**
   * Prints the availible courses.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestFagoversikt(Response response, CourseList courseList, List<String> args) {

    StringBuffer buffer = new StringBuffer("Fagoversikt: %S ");
    for (CourseItem courseItem : courseList) {
      String str = courseItem.getFagkode() + " - " + courseItem.getFagnavn();
      buffer.append(str);
      buffer.append(" %S ");
    }

    response.add(buffer.toString());
  }

  /**
   * Manage request where user wants information on permitted aids on the exam.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestHjelpemidler(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response,
            "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Hvilke hjelpemidler er tillatt i Statistikk?'");
        break;
      case "0":
        closeFagMatch(response, args, "hjelpemidler");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add("På eksamen i " + fagkode + " blir hjelpemidler: "
            + courseList.getCourseByFagkode(fagkode).getHjelpemidler());
        break;
      default:
        break;
    }
  }

  /**
   * Manage request where user wants to get some tips in the requested course.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestTips(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response, "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Har du noen tips i Fysikk?'");
        break;
      case "0":
        closeFagMatch(response, args, "tips");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add("Her har du noen tips i " + fagkode + ": " + courseList.getCourseByFagkode(fagkode).getTips());
        break;
      default:
        break;
    }
  }

  /**
   * Manage request where user wants information about the corricilum.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestPensum(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response, "Jeg forstår ikke helt, husk å spesifisere fag, f.eks 'Hva er pensum i TMA4140?'");
        break;
      case "0":
        closeFagMatch(response, args, "pensum");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add(
            "Dette er pensum i " + fagkode + ": " + courseList.getCourseByFagkode(fagkode).getAnbefaltLitteratur());
        break;
      default:
        break;
    }
  }

  /**
   * Manage request where user wants information about the grading of the exam.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestVurderingsform(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response,
            "Jeg forstår ikke helt, husk å spesifisere fag, f.eks: 'Hvilken vurderingsform er det i TMA4140?'");
        break;
      case "0":
        closeFagMatch(response, args, "vurderingsform");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add(
            "Vurderingsformen i " + fagkode + " er:  " + courseList.getCourseByFagkode(fagkode).getVurderingsform());
        break;
      default:
        break;
    }
  }

  /**
   * Identify if a String is a fagkode or not.
   * 
   * @param str String to check
   * @return true if str is a fagkode, false if not.
   */
  private static boolean identifyFagkode(String str) {
    return Pattern.compile("[a-zA-z]{2,4}\\d{4}").matcher(str).matches();
  }

  /**
   * Update response with generic message when no datamatch was found.
   * 
   * @param response active response Object.
   * @param message  message to print to user.
   */
  private static void dataMatchFailed(Response response, String message) {
    response.add(message);
    List<String[]> prompt = new ArrayList<>();
    prompt.add(new String[] { "fagoversikt", "fagoversikt" });
    response.setPrompt(prompt, null, null);
  }

  /**
   * If a close match to a course was found, update response with a default
   * response.
   * 
   * @param response active Response object.
   * @param args     response arguments containing our match data.
   * @param funcName funcName to call when the user selects a prompt option.
   */
  private static void closeFagMatch(Response response, List<String> args, String funcName) {

    String fagkode = identifyFagkode(args.get(1)) ? args.get(1) : args.get(2);

    // If the closest fagmatch is a fagkode, we need to uppercase the letters
    response.add("Jeg er litt usiker på hvilket fag du mente, mente du '"
        + (args.get(1).equals(fagkode) ? fagkode.toUpperCase() : args.get(1)) + "'?");
    response.setPrompt(List.of(new String[] { "ja", funcName }, new String[] { "nei", funcName }),
        List.of(fagkode.toUpperCase()), null);
  }

  /**
   * Manage request where user wants generic information about a subject.
   * 
   * @param response   active Response object
   * @param courseList active Courselist
   * @param args       arguments obtained from response.
   */
  private static void requestFaginfo(Response response, CourseList courseList, List<String> args) {
    switch (args.get(0)) {
      case "-1":
        dataMatchFailed(response,
            "Jeg forstår ikke hvilken informasjon du etterspør, husk å spesifisere hvilket fag du vil vite mer om.");
        break;
      case "0":
        closeFagMatch(response, args, "faginfo");
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1).toUpperCase() : args.get(2).toUpperCase();
        response.add("Her har du litt informasjon om " + fagkode + ": "
            + courseList.getCourseByFagkode(fagkode).getInformasjon());
        break;
      default:
        break;
    }
  }

}