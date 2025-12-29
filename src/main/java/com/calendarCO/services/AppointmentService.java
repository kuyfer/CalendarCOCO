package com.calendarCO.services;

import com.calendarCO.models.Appointment;
import com.calendarCO.models.dtos.AppointmentDTO;
import com.calendarCO.repositories.AppointmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentDTO.toEntity();
        Appointment saved = appointmentRepository.save(appointment);
        return AppointmentDTO.fromEntity(saved);
    }

    public void deleteAppointment(Long id) {
        appointmentRepository.deleteById(id);
    }

    public AppointmentDTO getAppointmentById(Long id) {
        return appointmentRepository.findById(id)
                .map(AppointmentDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }

    public List<AppointmentDTO> getAllAppointments() {
        return appointmentRepository.findAll().stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<AppointmentDTO> getAppointmentsByMonth(int year, int month) {
        java.time.LocalDateTime start = java.time.LocalDate.of(year, month, 1).atStartOfDay();
        java.time.LocalDateTime end = start.plusMonths(1);

        return appointmentRepository.findAppointmentsBetweenDates(start, end).stream()
                .map(AppointmentDTO::fromEntity)
                .collect(Collectors.toList());
    }
}