package com.calendarCO.repositories;

import com.calendarCO.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("SELECT a FROM Appointment a WHERE a.startTime >= :start AND a.startTime < :end ORDER BY a.startTime")
    List<Appointment> findAppointmentsBetweenDates(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );
}