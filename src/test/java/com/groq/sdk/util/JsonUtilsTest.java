package com.groq.sdk.util;

import com.groq.sdk.models.chat.ChatMessage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 11-Oct-2025
 *
 */
class JsonUtilsTest {

	@Test
	void testToJsonWithValidObject() {
		ChatMessage message = new ChatMessage("user", "Hello World");
		String json = JsonUtils.toJson(message);
		assertNotNull(json);
		assertTrue(json.contains("user"));
		assertTrue(json.contains("Hello World"));
	}

	@Test
	void testToJsonWithNull() {
		String json = JsonUtils.toJson(null);
		assertNull(json);
	}

	@Test
	void testFromJsonWithValidJson() {

		String json = "{\"role\":\"assistant\",\"content\":\"I'm here to help\"}";

		ChatMessage message = JsonUtils.fromJson(json, ChatMessage.class);

		assertNotNull(message);
		assertEquals("assistant", message.getRole());
		assertEquals("I'm here to help", message.getContent());
	}

	@Test
	void testFromJsonWithNull() {

		ChatMessage message = JsonUtils.fromJson(null, ChatMessage.class);

		assertNull(message);
	}

	@Test
	void testFromJsonWithEmptyString() {

		ChatMessage message = JsonUtils.fromJson("", ChatMessage.class);

		assertNull(message);
	}

	@Test
	void testToPrettyJson() {

		ChatMessage message = new ChatMessage("system", "You are helpful");

		String prettyJson = JsonUtils.toPrettyJson(message);

		assertNotNull(prettyJson);
		assertTrue(prettyJson.contains("system"));
		assertTrue(prettyJson.contains("You are helpful"));
	}

	@Test
	void testIsValidJsonWithValidJson() {

		String validJson = "{\"key\": \"value\"}";

		boolean isValid = JsonUtils.isValidJson(validJson);

		assertTrue(isValid);
	}

	@Test
	void testIsValidJsonWithInvalidJson() {

		String invalidJson = "{key: value}";

		boolean isValid = JsonUtils.isValidJson(invalidJson);

		assertFalse(isValid);
	}

	@Test
	void testIsValidJsonWithNull() {

		boolean isValid = JsonUtils.isValidJson(null);

		assertFalse(isValid);
	}
}