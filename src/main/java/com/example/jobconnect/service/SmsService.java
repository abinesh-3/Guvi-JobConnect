package com.example.jobconnect.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;

import jakarta.annotation.PostConstruct;

@Service
public class SmsService {

    @Value("${twilio.accountSid}")
    private String accountSid;
    @Value("${twilio.authToken}")
    private String authToken;
    @Value("${twilio.fromNumber}")
    private String fromNumber;

    @PostConstruct
    public void init() {
        if (accountSid != null && !accountSid.contains("YOUR_TWILIO")) {
            try {
                Twilio.init(accountSid, authToken);
            } catch(Exception e) {
                
            }
        }
    }

    public void sendSms(String to, String body){
        if (accountSid == null || accountSid.contains("YOUR_TWILIO")) {
            System.out.println("[SmsService] Skipped sending SMS (no credentials) to: " + to + " msg: " + body);
            return;
        }
        Message.creator(new com.twilio.type.PhoneNumber(to),
                        new com.twilio.type.PhoneNumber(fromNumber),
                        body).create();
    }
}
