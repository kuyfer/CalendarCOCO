package com.calendarCO.services;

import com.calendarCO.models.dtos.AppointmentDTO;
import com.calendarCO.models.dtos.CalendarDayDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalendarService {

    private final AppointmentService appointmentService;

    public CalendarService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public List<List<CalendarDayDTO>> generateCalendar(int year, int month) {
        List<List<CalendarDayDTO>> weeks = new ArrayList<>();
        List<CalendarDayDTO> currentWeek = new ArrayList<>();

        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);

        // Get appointments for the month
        List<AppointmentDTO> appointments = appointmentService.getAppointmentsByMonth(year, month);

        // Calculate starting day (Monday = 1, Sunday = 7 in Java's DayOfWeek)
        int startOffset = firstDay.getDayOfWeek().getValue() - 1; // Adjust to Monday start

        // Add days from previous month
        LocalDate previousMonth = firstDay.minusMonths(1);
        YearMonth prevYearMonth = YearMonth.of(previousMonth.getYear(), previousMonth.getMonth());
        int daysInPrevMonth = prevYearMonth.lengthOfMonth();

        for (int i = startOffset - 1; i >= 0; i--) {
            int day = daysInPrevMonth - i;
            currentWeek.add(new CalendarDayDTO(
                    prevYearMonth.getYear(),
                    prevYearMonth.getMonthValue(),
                    day,
                    true
            ));
        }

        // Add days for current month
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            if (currentWeek.size() == 7) {
                weeks.add(currentWeek);
                currentWeek = new ArrayList<>();
            }

            CalendarDayDTO calendarDay = new CalendarDayDTO(year, month, day, false);

            // Add appointments for this day
            LocalDate currentDate = LocalDate.of(year, month, day);
            appointments.stream()
                    .filter(apt -> apt.getStartTime().toLocalDate().equals(currentDate))
                    .forEach(calendarDay::addAppointment);

            currentWeek.add(calendarDay);
        }

        // Add days from next month to complete the week
        int nextMonthDay = 1;
        while (currentWeek.size() < 7) {
            int nextMonth = month == 12 ? 1 : month + 1;
            int nextYear = month == 12 ? year + 1 : year;

            currentWeek.add(new CalendarDayDTO(
                    nextYear,
                    nextMonth,
                    nextMonthDay++,
                    true
            ));
        }

        if (!currentWeek.isEmpty()) {
            weeks.add(currentWeek);
        }

        return weeks;
    }

    public MonthCalendarDTO getMonthCalendar(int year, int month) {
        List<List<CalendarDayDTO>> weeks = generateCalendar(year, month);
        LocalDate currentDate = LocalDate.now();

        return new MonthCalendarDTO(
                weeks,
                year,
                month,
                LocalDate.of(year, month, 1).getMonth().toString(),
                currentDate.getYear() == year && currentDate.getMonthValue() == month
        );
    }

    public static class MonthCalendarDTO {
        private final List<List<CalendarDayDTO>> weeks;
        private final int year;
        private final int month;
        private final String monthName;
        private final boolean isCurrentMonth;

        public MonthCalendarDTO(List<List<CalendarDayDTO>> weeks, int year, int month,
                                String monthName, boolean isCurrentMonth) {
            this.weeks = weeks;
            this.year = year;
            this.month = month;
            this.monthName = monthName;
            this.isCurrentMonth = isCurrentMonth;
        }

        // Getters
        public List<List<CalendarDayDTO>> getWeeks() { return weeks; }
        public int getYear() { return year; }
        public int getMonth() { return month; }
        public String getMonthName() { return monthName; }
        public boolean isCurrentMonth() { return isCurrentMonth; }
    }
}