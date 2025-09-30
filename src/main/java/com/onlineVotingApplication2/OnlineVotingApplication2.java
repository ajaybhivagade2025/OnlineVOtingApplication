package com.onlineVotingApplication2;

import com.onlineVotingApplication2.entity.User;
import com.onlineVotingApplication2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OnlineVotingApplication2 implements CommandLineRunner {

    @Autowired
    private UserService userService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineVotingApplication2.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Create default admin user if not exists
        if (!userService.usernameExists("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setRole("ADMIN");
            admin.setHasVoted(false);
            admin.setVerified(true);
            userService.createAdmin(admin);
            System.out.println("Default admin user created: admin/admin123");
        }
    }
}