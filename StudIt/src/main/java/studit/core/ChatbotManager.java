package studit.core;

public class ChatbotManager {
	
	public void manageInput(String input) {
		// Splitting string by spaces, and removing all newline chars
		String[] command = input.replaceAll("\n", "").split(" ");
		System.out.println(command);
		
	}

}
