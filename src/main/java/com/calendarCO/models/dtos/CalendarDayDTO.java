package com.calendarCO.models.dtos;

import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class CalendarDayDTO {
    private int year;
    private int month;
    private int day;
    private boolean otherMonth;
    private List<AppointmentDTO> appointments = new ArrayList<>();

    public CalendarDayDTO(int year, int month, int day, boolean otherMonth) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.otherMonth = otherMonth;
    }

    public void addAppointment(AppointmentDTO appointment) {
        this.appointments.add(appointment);
    }

    public String getDateString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }
}