package com.calendarCO.services;

import com.calendarCO.models.Appointment;
import com.calendarCO.models.dtos.AppointmentDTO;
import com.calendarCO.models.dtos.UserDTO;
import com.calendarCO.repositories.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final UserIntegrationService userIntegrationService;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              UserIntegrationService userIntegrationService) {
        this.appointmentRepository = appointmentRepository;
        this.userIntegrationService = userIntegrationService;
    }

    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        log.info("Creating appointment: {}", appointmentDTO.getTitle());

        // Validate user exists if userId is provided
        if (appointmentDTO.getUserId() != null) {
            try {
                UserDTO user = userIntegrationService.getUserById(appointmentDTO.getUserId());
                if (user == null) {
                    throw new RuntimeException("User not found with id: " + appointmentDTO.getUserId());
                }
                log.info("Validated user {} for appointment", user.getUsername());
            } catch (Exception e) {
                log.error("Error validating user for appointment creation", e);
                throw new RuntimeException("Cannot validate user: " + e.getMessage(), e);
            }
        }

        Appointment appointment = appointmentDTO.toEntity();
        Appointment saved = appointmentRepository.save(appointment);
        log.info("Successfully created appointment with id: {}", saved.getId());

        return AppointmentDTO.fromEntity(saved);
    }

    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        log.info("Updating appointment with id: {}", id);

        // Find existing appointment
        Appointment existingAppointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // Validate user exists if userId is being updated
        if (appointmentDTO.getUserId() != null &&
                !appointmentDTO.getUserId().equals(existingAppointment.getUserId())) {
            try {
                UserDTO user = userIntegrationService.getUserById(appointmentDTO.getUserId());
                if (user == null) {
                    throw new RuntimeException("User not found with id: " + appointmentDTO.getUserId());
                }
                log.info("Validated new user {} for appointment update", user.getUsername());
            } catch (Exception e) {
                log.error("Error validating user for appointment update", e);
                throw new RuntimeException("Cannot validate user: " + e.getMessage(), e);
            }
        }

        // Update fields from DTO
        existingAppointment.setTitle(appointmentDTO.getTitle());
        existingAppointment.setDescription(appointmentDTO.getDescription());
        existingAppointment.setStartTime(appointmentDTO.getStartTime());
        existingAppointment.setEndTime(appointmentDTO.getEndTime());

        // Update userId if provided
        if (appointmentDTO.getUserId() != null) {
            existingAppointment.setUserId(appointmentDTO.getUserId());
        }

        // Save and return updated appointment
        Appointment updated = appointmentRepository.save(existingAppointment);
        log.info("Successfully updated appointment with id: {}", updated.getId());

        return AppointmentDTO.fromEntity(updated);
    }

    public void deleteAppointment(Long id) {
        log.info("Deleting appointment with id: {}", id);

        // Check if appointment exists before deletion
        if (!appointmentRepository.existsById(id)) {
            throw new RuntimeException("Appointment not found with id: " + id);
        }

        appointmentRepository.deleteById(id);
        log.info("Successfully deleted appointment with id: {}", id);
    }

    public AppointmentDTO getAppointmentById(Long id) {
        log.debug("Fetching appointment with id: {}", id);

        AppointmentDTO appointmentDTO = appointmentRepository.findById(id)
                .map(AppointmentDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));

        // Enrich with user information if userId exists
        if (appointmentDTO.getUserId() != null) {
            try {
                UserDTO user = userIntegrationService.getUserById(appointmentDTO.getUserId());
                if (user != null) {
                    appointmentDTO.setUserName(user.getUsername());
                    appointmentDTO.setUserEmail(user.getEmail());
                }
            } catch (Exception e) {
                log.warn("Could not fetch user details for appointment {}: {}", id, e.getMessage());
                // Don't fail the request if user service is down
            }
        }

        return appointmentDTO;
    }

    public List<AppointmentDTO> getAllAppointments() {
        log.debug("Fetching all appointments");

        List<AppointmentDTO> appointments = appointmentRepository.findAll().stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());

        // Enrich with user information
        enrichAppointmentsWithUserData(appointments);

        return appointments;
    }

    public List<AppointmentDTO> getAppointmentsByMonth(int year, int month) {
        log.debug("Fetching appointments for {}/{}", year, month);

        java.time.LocalDateTime start = java.time.LocalDate.of(year, month, 1).atStartOfDay();
        java.time.LocalDateTime end = start.plusMonths(1);

        List<AppointmentDTO> appointments = appointmentRepository.findAppointmentsBetweenDates(start, end).stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());

        // Enrich with user information
        enrichAppointmentsWithUserData(appointments);

        return appointments;
    }

    public List<AppointmentDTO> getAppointmentsByUser(Long userId) {
        log.debug("Fetching appointments for user: {}", userId);

        // Validate user exists
        try {
            UserDTO user = userIntegrationService.getUserById(userId);
            if (user == null) {
                throw new RuntimeException("User not found with id: " + userId);
            }
        } catch (Exception e) {
            log.error("Error validating user for appointment fetch", e);
            throw new RuntimeException("Cannot validate user: " + e.getMessage(), e);
        }

        return appointmentRepository.findByUserId(userId).stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByUserAndMonth(Long userId, int year, int month) {
        log.debug("Fetching appointments for user {} in {}/{}", userId, year, month);

        // Validate user exists
        try {
            UserDTO user = userIntegrationService.getUserById(userId);
            if (user == null) {
                throw new RuntimeException("User not found with id: " + userId);
            }
        } catch (Exception e) {
            log.error("Error validating user for appointment fetch", e);
            throw new RuntimeException("Cannot validate user: " + e.getMessage(), e);
        }

        java.time.LocalDateTime start = java.time.LocalDate.of(year, month, 1).atStartOfDay();
        java.time.LocalDateTime end = start.plusMonths(1);

        return appointmentRepository.findByUserIdAndStartTimeBetween(userId, start, end).stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    private void enrichAppointmentsWithUserData(List<AppointmentDTO> appointments) {
        for (AppointmentDTO appointment : appointments) {
            if (appointment.getUserId() != null) {
                try {
                    UserDTO user = userIntegrationService.getUserById(appointment.getUserId());
                    if (user != null) {
                        appointment.setUserName(user.getUsername());
                        appointment.setUserEmail(user.getEmail());
                    }
                } catch (Exception e) {
                    log.warn("Could not fetch user details for appointment {}: {}",
                            appointment.getId(), e.getMessage());
                    // Continue processing other appointments even if one user fetch fails
                }
            }
        }
    }
}
