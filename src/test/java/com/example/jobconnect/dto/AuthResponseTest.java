package com.example.jobconnect.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class AuthResponseTest {
	private String token;
	private String type;

	public AuthResponseTest(String token, String type) {
		this.token = token;
		this.type = type;
	}

	public String getToken() { return token; }
	public String getType() { return type; }

	@Override
	public String toString() {
		return "AuthResponseTest{" +
				"token='" + token + '\'' +
				", type='" + type + '\'' +
				'}';
	}

	
	public static class AuthResponseTestUnit {
		@Test
		void testGetters() {
			AuthResponseTest resp = new AuthResponseTest("abc123", "Bearer");
			assertEquals("abc123", resp.getToken());
			assertEquals("Bearer", resp.getType());
		}

		@Test
		void testToString() {
			AuthResponseTest resp = new AuthResponseTest("xyz789", "Bearer");
			String str = resp.toString();
			assertTrue(str.contains("xyz789"));
			assertTrue(str.contains("Bearer"));
		}

		@Test
		void testWithMockito() {
			AuthResponseTest mockResp = Mockito.mock(AuthResponseTest.class);
			when(mockResp.getToken()).thenReturn("mocktoken");
			when(mockResp.getType()).thenReturn("MockType");
			assertEquals("mocktoken", mockResp.getToken());
			assertEquals("MockType", mockResp.getType());
		}
	}
}
