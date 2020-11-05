package studit.core.chatbot;

import java.util.List;
import java.util.regex.Pattern;
import studit.core.mainpage.CourseList;

public class InformationRequestExecutor {

  public static Response executeCommand(Response response, CourseList courseList) {

    switch (response.getFuncKey()) {
      case "faginfo":
        requestFaginfo(response, courseList);
        break;
      default:
        break;
    }

    return null;
  }

  private static boolean identifyFagkode(String str) {
    return Pattern.compile("[a-zA-z]{2,4}\\d{4}").matcher(str).matches();
  }

  private static void requestFaginfo(Response response, CourseList courseList) {
    List<String> args = response.getArguments();
    switch (args.get(0)) {
      case "-1":
        response.add("Jeg forstår ikke hvilken informasjon du etterspør, vennligst oppgi mer informasjon.");
        break;
      case "0":
        response.add("Jeg er litt usiker på hva du mener, ønsker du å vite mer om '" + args.get(1) + "'?");
        response.setPrompt(List.of(new String[] {"ja", "courseInfo"}, new String[] {"nei", "courseInfo"}));
        break;
      case "1":
        String fagkode = identifyFagkode(args.get(1)) ? args.get(1) : args.get(2);
        response.add("Her har du litt informasjon om " + fagkode + ": "
            + courseList.getCourseByFagkode(fagkode.toUpperCase()).getInformasjon());
      default:
        break;
    }
  }

}