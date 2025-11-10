package com.example.jobconnect.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    private static final String ACCOUNT_SID = "test_account_sid";
    private static final String AUTH_TOKEN = "test_auth_token";
    private static final String FROM_NUMBER = "+1234567890";
    private static final String TO_NUMBER = "+9876543210";
    private static final String TEST_MESSAGE = "Test message";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(smsService, "accountSid", ACCOUNT_SID);
        ReflectionTestUtils.setField(smsService, "authToken", AUTH_TOKEN);
        ReflectionTestUtils.setField(smsService, "fromNumber", FROM_NUMBER);
    }

    @Test
    void testInitWithValidCredentials() {
        try (MockedStatic<Twilio> twilioStatic = mockStatic(Twilio.class)) {
            smsService.init();

            twilioStatic.verify(() -> 
                Twilio.init(ACCOUNT_SID, AUTH_TOKEN)
            );
        }
    }

    @Test
    void testInitWithInvalidCredentials() {
        ReflectionTestUtils.setField(smsService, "accountSid", "YOUR_TWILIO_ACCOUNT_SID");
        
        try (MockedStatic<Twilio> twilioStatic = mockStatic(Twilio.class)) {
            smsService.init();

            twilioStatic.verifyNoInteractions();
        }
    }

    @Test
    void testSendSmsWithValidCredentials() {
        Message mockedMessage = mock(Message.class);
        MessageCreator mockedCreator = mock(MessageCreator.class);
        when(mockedCreator.create()).thenReturn(mockedMessage);

        try (MockedStatic<Message> messageStatic = mockStatic(Message.class)) {
            messageStatic.when(() -> Message.creator(
                any(PhoneNumber.class),
                any(PhoneNumber.class),
                eq(TEST_MESSAGE)
            )).thenReturn(mockedCreator);

            smsService.sendSms(TO_NUMBER, TEST_MESSAGE);

            messageStatic.verify(() -> Message.creator(
                any(PhoneNumber.class),
                any(PhoneNumber.class),
                eq(TEST_MESSAGE)
            ));
            verify(mockedCreator).create();
        }
    }

    @Test
    void testSendSmsWithInvalidCredentials() {
        ReflectionTestUtils.setField(smsService, "accountSid", "YOUR_TWILIO_ACCOUNT_SID");

        try (MockedStatic<Message> messageStatic = mockStatic(Message.class)) {
            smsService.sendSms(TO_NUMBER, TEST_MESSAGE);

            messageStatic.verifyNoInteractions();
        }
    }

    @Test
    void testSendSmsWithNullCredentials() {
        
        ReflectionTestUtils.setField(smsService, "accountSid", null);

        try (MockedStatic<Message> messageStatic = mockStatic(Message.class)) {
            
            smsService.sendSms(TO_NUMBER, TEST_MESSAGE);

            
            messageStatic.verifyNoInteractions();
        }
    }

    @Test
    void testInitWithTwilioException() {
        try (MockedStatic<Twilio> twilioStatic = mockStatic(Twilio.class)) {
            twilioStatic.when(() -> Twilio.init(anyString(), anyString()))
                       .thenThrow(new RuntimeException("Twilio initialization failed"));

            smsService.init();

            
            twilioStatic.verify(() -> Twilio.init(ACCOUNT_SID, AUTH_TOKEN));
        }
    }

    @Test
    void testSendSmsWithTwilioException() {
        
        MessageCreator mockedCreator = mock(MessageCreator.class);
        when(mockedCreator.create()).thenThrow(new RuntimeException("Failed to send SMS"));

        try (MockedStatic<Message> messageStatic = mockStatic(Message.class)) {
            messageStatic.when(() -> Message.creator(
                any(PhoneNumber.class),
                any(PhoneNumber.class),
                anyString()
            )).thenReturn(mockedCreator);

            
            smsService.sendSms(TO_NUMBER, TEST_MESSAGE);

            
            verify(mockedCreator).create();
        }
    }
}
