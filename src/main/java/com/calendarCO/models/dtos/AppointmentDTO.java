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
        return dto;
    }
}