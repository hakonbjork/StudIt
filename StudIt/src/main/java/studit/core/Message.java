package studit.core;

import studit.ui.ChatbotController;

public class Message {
	
	private String text;
    private String user;

    public Message(String text, String user) {
        this.text = text;
        this.user = user;
    }

    public String getText() {
    	
    	text = text.replace("\n", "");
    	String output = "";
    	for (int i = 0; i < text.length(); i++) {
    		output += text.charAt(i);
    		if (i % (ChatbotController.lineBreakLength - 8) == 0 && i != 0) {
    			output += "\n";
    		}
    	}
        return output;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
