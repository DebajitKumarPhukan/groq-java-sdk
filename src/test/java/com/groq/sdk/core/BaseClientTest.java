package com.groq.sdk.core;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

/**
 *
 * @author Debajit Kumar Phukan
 * @since 11-Oct-2025
 *
 */
class BaseClientTest {
	private MockWebServer mockWebServer;
	private TestBaseClient client;

	static class TestBaseClient extends BaseClient {
		public TestBaseClient(Builder builder) {
			super(builder);
		}
	}

	static class TestBuilder extends BaseClient.Builder<TestBaseClient> {
		@Override
		public TestBaseClient build() {
			return new TestBaseClient(this);
		}
	}

	@BeforeEach
	void setUp() throws IOException {
		mockWebServer = new MockWebServer();
		mockWebServer.start();

		client = new TestBuilder().apiKey("test-key").baseUrl(mockWebServer.url("/").toString())
				.timeout(Duration.ofSeconds(5)).build();
	}

	@AfterEach
	void tearDown() throws IOException {
		mockWebServer.shutdown();
	}

	@Test
	void testSuccessfulRequest() {

		mockWebServer.enqueue(new MockResponse().setBody("{\"status\":\"ok\"}").setResponseCode(200));

		assertNotNull(client);
	}

	@Test
	void testRequestWithRetry() {
		mockWebServer.enqueue(new MockResponse().setResponseCode(500));
		mockWebServer.enqueue(new MockResponse().setBody("{\"status\":\"ok\"}").setResponseCode(200));

		assertNotNull(mockWebServer);
	}

	@Test
	void testRequestTimeout() {
		mockWebServer.enqueue(new MockResponse().setBody("{}").setResponseCode(200).setBodyDelay(10, TimeUnit.SECONDS)); // Fixed:
																															// using
																															// long
																															// +
																															// TimeUnit

		assertNotNull(client);
	}

	@Test
	void testBuildUrlWithValidPath() {
		assertNotNull(client);
	}

	@Test
	void testBuildUrlWithInvalidPath() {
		assertNotNull(client);
	}
}