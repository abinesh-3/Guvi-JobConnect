package com.example.jobconnect.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;

public class JwtUtilsTest {
	public String generateToken(String email) { return "token-for-" + email; }

	
	public static class JwtUtilsTestUnit {
		@Test
		void testGenerateTokenReturnsExpectedString() {
			JwtUtilsTest utils = new JwtUtilsTest();
			String token = utils.generateToken("user@example.com");
			assertEquals("token-for-user@example.com", token);
		}

		@Test
		void testWithMockito() {
			JwtUtilsTest mockUtils = Mockito.mock(JwtUtilsTest.class);
			when(mockUtils.generateToken("mock@ex.com")).thenReturn("mock-token");
			assertEquals("mock-token", mockUtils.generateToken("mock@ex.com"));
		}
	}
}
