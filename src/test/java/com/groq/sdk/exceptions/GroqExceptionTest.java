package com.groq.sdk.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 11-Oct-2025
 *
 */
class GroqExceptionTest {

	@Test
	void testConstructorWithMessageAndStatusCode() {

		GroqException exception = new GroqException("API Error", 400, "Bad Request");

		assertEquals("API Error", exception.getMessage());
		assertEquals(400, exception.getStatusCode());
		assertEquals("Bad Request", exception.getResponseBody());
	}

	@Test
	void testConstructorWithMessageAndCause() {

		Throwable cause = new RuntimeException("Root cause");

		GroqException exception = new GroqException("Operation failed", cause);

		assertEquals("Operation failed", exception.getMessage());
		assertEquals(cause, exception.getCause());
		assertEquals(0, exception.getStatusCode());
		assertNull(exception.getResponseBody());
	}

	@Test
	void testToString() {

		GroqException exception = new GroqException("Test Error", 500, "Server Error");

		String toString = exception.toString();

		assertTrue(toString.contains("Test Error"));
		assertTrue(toString.contains("500"));
		assertTrue(toString.contains("Server Error"));
	}
}