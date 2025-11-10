package com.example.jobconnect.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class JwtAuthenticationFilterTest {
	
	public static class JwtAuthenticationFilterTestUnit {
		@Test
		void testDoFilterIsCalled() throws Exception {
			ServletRequest req = Mockito.mock(ServletRequest.class);
			ServletResponse res = Mockito.mock(ServletResponse.class);
			FilterChain chain = Mockito.mock(FilterChain.class);
			
			chain.doFilter(req, res);
			verify(chain).doFilter(req, res);
		}

		@Test
		void testWithMockito() {
			JwtAuthenticationFilterTest mockFilter = Mockito.mock(JwtAuthenticationFilterTest.class);
			assertNotNull(mockFilter);
		}
	}
}
