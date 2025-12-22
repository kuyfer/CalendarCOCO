package com.calendarCO.controllers;

import com.calendarCO.models.dtos.AppointmentDTO;
import com.calendarCO.models.dtos.CalendarRequest;
import com.calendarCO.services.AppointmentService;
import com.calendarCO.services.CalendarService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final AppointmentService appointmentService;

    public CalendarController(CalendarService calendarService,
                              AppointmentService appointmentService) {
        this.calendarService = calendarService;
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public String viewCalendar(Model model) {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();

        return viewCalendar(year, month, model);
    }

    @GetMapping("/{year}/{month}")
    public String viewCalendar(@PathVariable int year,
                               @PathVariable int month,
                               Model model) {
        CalendarService.MonthCalendarDTO calendar = calendarService.getMonthCalendar(year, month);

        model.addAttribute("calendar", calendar);
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("appointmentDTO", new AppointmentDTO());
        model.addAttribute("calendarRequest", new CalendarRequest());

        return "calendar";
    }

    @PostMapping("/navigate")
    public String navigateCalendar(@ModelAttribute CalendarRequest request) {
        return "redirect:/calendar/" + request.getYear() + "/" + request.getMonth();
    }

    @PostMapping("/appointments")
    public String createAppointment(@Valid @ModelAttribute AppointmentDTO appointmentDTO,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    Model model) {
        if (result.hasErrors()) {
            LocalDate today = LocalDate.now();
            CalendarService.MonthCalendarDTO calendar =
                    calendarService.getMonthCalendar(today.getYear(), today.getMonthValue());

            model.addAttribute("calendar", calendar);
            model.addAttribute("currentDate", today);
            model.addAttribute("calendarRequest", new CalendarRequest());
            return "calendar";
        }

        appointmentService.createAppointment(appointmentDTO);
        redirectAttributes.addFlashAttribute("successMessage", "Appointment created successfully!");

        LocalDate startDate = appointmentDTO.getStartTime().toLocalDate();
        return "redirect:/calendar/" + startDate.getYear() + "/" + startDate.getMonthValue();
    }

    @GetMapping("/appointments/delete/{id}")
    public String deleteAppointment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            appointmentService.deleteAppointment(id);
            redirectAttributes.addFlashAttribute("successMessage", "Appointment deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting appointment: " + e.getMessage());
        }

        return "redirect:/calendar";
    }

    @GetMapping("/today")
    public String goToToday() {
        LocalDate today = LocalDate.now();
        return "redirect:/calendar/" + today.getYear() + "/" + today.getMonthValue();
    }
}