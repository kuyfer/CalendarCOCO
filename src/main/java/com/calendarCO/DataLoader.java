package com.calendarCO;

import com.calendarCO.models.Appointment;
import com.calendarCO.models.User;
import com.calendarCO.repositories.AppointmentRepository;
import com.calendarCO.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository,
                                   AppointmentRepository appointmentRepository) {
        return args -> {
            // Create sample users
            User user1 = new User(null, "john_doe", "john@example.com", "John Doe", "Engineering");
            User user2 = new User(null, "jane_smith", "jane@example.com", "Jane Smith", "Marketing");
            User user3 = new User(null, "bob_wilson", "bob@example.com", "Bob Wilson", "Sales");

            userRepository.save(user1);
            userRepository.save(user2);
            userRepository.save(user3);

            // Create sample appointments for current month
            LocalDateTime now = LocalDateTime.now();
            int currentYear = now.getYear();
            int currentMonth = now.getMonthValue();

            Appointment apt1 = new Appointment();
            apt1.setTitle("Team Meeting");
            apt1.setDescription("Weekly team sync");
            apt1.setStartTime(LocalDateTime.of(currentYear, currentMonth, 17, 9, 0));
            apt1.setEndTime(LocalDateTime.of(currentYear, currentMonth, 17, 10, 0));
            apt1.setLocation("Conference Room A");
            apt1.setColorCode("#2C87F2");
            apt1.setParticipants("John Doe, Jane Smith");
            apt1.setType(Appointment.AppointmentType.MEETING);

            Appointment apt2 = new Appointment();
            apt2.setTitle("Client Call");
            apt2.setDescription("Discuss project requirements");
            apt2.setStartTime(LocalDateTime.of(currentYear, currentMonth, 18, 14, 0));
            apt2.setEndTime(LocalDateTime.of(currentYear, currentMonth, 18, 15, 0));
            apt2.setLocation("Zoom Meeting");
            apt2.setColorCode("#4CAF50");
            apt2.setParticipants("Client ABC, Bob Wilson");
            apt2.setType(Appointment.AppointmentType.CALL);

            Appointment apt3 = new Appointment();
            apt3.setTitle("Code Review");
            apt3.setDescription("Review PR #1234");
            apt3.setStartTime(LocalDateTime.of(currentYear, currentMonth, 19, 11, 0));
            apt3.setEndTime(LocalDateTime.of(currentYear, currentMonth, 19, 12, 0));
            apt3.setLocation("Slack");
            apt3.setColorCode("#FF5722");
            apt3.setParticipants("John Doe, Jane Smith");
            apt3.setType(Appointment.AppointmentType.REVIEW);

            appointmentRepository.save(apt1);
            appointmentRepository.save(apt2);
            appointmentRepository.save(apt3);

            System.out.println("Sample data loaded successfully!");
        };
    }
}