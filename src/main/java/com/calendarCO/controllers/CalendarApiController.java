package com.calendarCO.controllers;

import com.calendarCO.models.dtos.AppointmentDTO;
import com.calendarCO.models.dtos.CalendarRequest;
import com.calendarCO.services.AppointmentService;
import com.calendarCO.services.CalendarService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200") // Angular dev server
public class CalendarApiController {

    private final CalendarService calendarService;
    private final AppointmentService appointmentService;

    public CalendarApiController(CalendarService calendarService,
                                 AppointmentService appointmentService) {
        this.calendarService = calendarService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/calendar/current")
    public ResponseEntity<?> getCurrentMonth() {
        LocalDate today = LocalDate.now();
        CalendarService.MonthCalendarDTO calendar =
                calendarService.getMonthCalendar(today.getYear(), today.getMonthValue());
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/calendar/{year}/{month}")
    public ResponseEntity<?> getMonthCalendar(@PathVariable int year,
                                              @PathVariable int month) {
        CalendarService.MonthCalendarDTO calendar =
                calendarService.getMonthCalendar(year, month);
        return ResponseEntity.ok(calendar);
    }

    @PostMapping("/appointments")
    public ResponseEntity<?> createAppointment(@Valid @RequestBody AppointmentDTO appointmentDTO) {
        appointmentService.createAppointment(appointmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created successfully");
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable Long id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.ok("Appointment deleted successfully");
    }

    // Optional: Add PUT for updating appointments
    @PutMapping("/appointments/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable Long id,
                                               @Valid @RequestBody AppointmentDTO appointmentDTO) {
        appointmentService.updateAppointment(id, appointmentDTO);
        return ResponseEntity.ok("Appointment updated successfully");
    }
}
