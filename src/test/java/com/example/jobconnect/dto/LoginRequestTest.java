package com.example.jobconnect.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginRequestTest {
    private String email;
    private String password;

    public LoginRequestTest() {}
    public LoginRequestTest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String email() { return email; }
    public String password() { return password; }

    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    public static class LoginRequestTestUnit {
        private LoginRequestTest loginRequest;

        @BeforeEach
        void setUp() {
            loginRequest = new LoginRequestTest();
        }

        @Test
        void testDefaultConstructor() {
            assertNull(loginRequest.email());
            assertNull(loginRequest.password());
        }

        @Test
        void testParameterizedConstructor() {
            LoginRequestTest request = new LoginRequestTest("test@example.com", "password123");
            assertEquals("test@example.com", request.email());
            assertEquals("password123", request.password());
        }

        @Test
        void testSetAndGetEmail() {
            loginRequest.setEmail("user@example.com");
            assertEquals("user@example.com", loginRequest.email());
        }

        @Test
        void testSetAndGetPassword() {
            loginRequest.setPassword("pass123");
            assertEquals("pass123", loginRequest.password());
        }

        @Test
        void testNullValues() {
            loginRequest.setEmail(null);
            loginRequest.setPassword(null);
            assertNull(loginRequest.email());
            assertNull(loginRequest.password());
        }

        @Test
        void testCompleteRequestCreation() {
            loginRequest.setEmail("complete@test.com");
            loginRequest.setPassword("completePass");
            assertEquals("complete@test.com", loginRequest.email());
            assertEquals("completePass", loginRequest.password());
        }

        @Test
        void testWithMockito() {
            LoginRequestTest mockRequest = mock(LoginRequestTest.class);
            when(mockRequest.email()).thenReturn("mock@example.com");
            when(mockRequest.password()).thenReturn("mockpass");
            
            assertEquals("mock@example.com", mockRequest.email());
            assertEquals("mockpass", mockRequest.password());
            
            verify(mockRequest, times(1)).email();
            verify(mockRequest, times(1)).password();
        }

        @Test
        void testEmailValidation() {
            loginRequest.setEmail("");
            assertEquals("", loginRequest.email(), "Empty email should be allowed");
            
            loginRequest.setEmail("test@example.com");
            assertEquals("test@example.com", loginRequest.email(), "Valid email should be stored");
        }

        @Test
        void testPasswordValidation() {
            loginRequest.setPassword("");
            assertEquals("", loginRequest.password(), "Empty password should be allowed");
            
            loginRequest.setPassword("securePassword123");
            assertEquals("securePassword123", loginRequest.password(), "Valid password should be stored");
        }
	}
}
