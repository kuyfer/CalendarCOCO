
package com.calendarCO.models.dtos;

import com.calendarCO.models.Appointment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    private LocalDateTime endTime;

    private String location;
    private String colorCode;
    private String participants;
    private Appointment.AppointmentType type;

    // User integration fields
    private Long userId;
    private String userName;
    private String userEmail;
    private String userFullName;

    // Convert to Entity
    public Appointment toEntity() {
        Appointment appointment = new Appointment();
        appointment.setId(this.id);
        appointment.setTitle(this.title);
        appointment.setDescription(this.description);
        appointment.setStartTime(this.startTime);
        appointment.setEndTime(this.endTime);
        appointment.setLocation(this.location);
        appointment.setColorCode(this.colorCode != null ? this.colorCode : "#2C87F2");
        appointment.setParticipants(this.participants);
        appointment.setType(this.type != null ? this.type : Appointment.AppointmentType.MEETING);
        appointment.setUserId(this.userId);
        return appointment;
    }

    // Convert from Entity
    public static AppointmentDTO fromEntity(Appointment appointment) {
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setTitle(appointment.getTitle());
        dto.setDescription(appointment.getDescription());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setLocation(appointment.getLocation());
        dto.setColorCode(appointment.getColorCode());
        dto.setParticipants(appointment.getParticipants());
        dto.setType(appointment.getType());
        dto.setUserId(appointment.getUserId());
        return dto;
    }

    // Helper method to get display name for user
    public String getUserDisplayName() {
        if (userFullName != null && !userFullName.trim().isEmpty()) {
            return userFullName;
        }
        if (userName != null && !userName.trim().isEmpty()) {
            return userName;
        }
        return "Unknown User";
    }

    // Helper method to check if appointment has assigned user
    public boolean hasAssignedUser() {
        return userId != null;
    }

    // Helper method for frontend display
    public String getFormattedParticipants() {
        StringBuilder participants = new StringBuilder();

        if (hasAssignedUser()) {
            participants.append("Assigned to: ").append(getUserDisplayName());
        }

        if (this.participants != null && !this.participants.trim().isEmpty()) {
            if (participants.length() > 0) {
                participants.append(", ");
            }
            participants.append("Participants: ").append(this.participants);
        }

        return participants.toString();
    }
}
