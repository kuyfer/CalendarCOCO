package com.calendarCO.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    private String location;

    @Column(name = "color_code")
    private String colorCode = "#2C87F2";

    private String participants;

    @Enumerated(EnumType.STRING)
    private AppointmentType type = AppointmentType.MEETING;

    // User integration field
    @Column(name = "user_id")
    private Long userId;

    public enum AppointmentType {
        MEETING, CALL, REVIEW, TASK, OTHER
    }
}
