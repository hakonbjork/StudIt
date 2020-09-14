package studit.core;

public class ChatbotManager {
	
	public String manageInput(String input) {
		// Splitting string by spaces, and removing all newline chars
		String[] command = input.replaceAll("\n", "").toLowerCase().split(" ");
		
		
		String response = "";
		
		for (String word : command) {
			if (word.contains("hei")) {
				response += "Hei. ";
				break;
			}
		}
		
		
		if (response.equals("")) {
			response += "Jeg beklager, men det forstod jeg ikke helt. Kanskje du mente...?";
		}
		return response;
		
	}

}
