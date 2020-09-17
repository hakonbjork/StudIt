package core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import studit.core.Message;

class MessageTest {
	
	private Message chatbotMessage;
	private Message userMessage;
	
	@BeforeEach
	public void init() {
		chatbotMessage = new Message("chatbot", "");
		userMessage = new Message("user", "");
	}

	@Test
	void testGetText() {
		String testMsg = "test-message";
		chatbotMessage.setText(testMsg);
		userMessage.setText(testMsg);
		
		assertEquals(chatbotMessage.getText(), testMsg);
		assertEquals(userMessage.getText(), testMsg);
	}

	@Test
	void testSetText() {
		fail("Not yet implemented");
	}

	@Test
	void testGetUser() {
		fail("Not yet implemented");
	}

	@Test
	void testSetUser() {
		fail("Not yet implemented");
	}

}
