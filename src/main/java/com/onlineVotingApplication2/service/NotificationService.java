package com.onlineVotingApplication2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

        @Autowired
        private JavaMailSender mailSender;

        public void notifyByEmail(String toEmail, String message) {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Voting System Notification");
            msg.setText(message);
            mailSender.send(msg);
        }

        public void notifyBySms(String phone, String message) {
            System.out.println("SMS to " + phone + " : " + message);
        }
    }


