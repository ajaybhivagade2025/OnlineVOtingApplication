package com.onlineVotingApplication2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OtpService {



        @Autowired
        private JavaMailSender mailSender;

        public String generateOtp() {
            return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit
        }

        public void sendEmailOtp(String toEmail, String otp) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Voting System OTP");
            msg.setText("Your OTP is: " + otp);
            mailSender.send(msg);
        }

        public void sendSmsOtp(String phone, String otp) {
            // For dev: print to logs. For production: integrate Twilio (example in comments).
            System.out.println("SMS to " + phone + " OTP: " + otp);
        }
    }



