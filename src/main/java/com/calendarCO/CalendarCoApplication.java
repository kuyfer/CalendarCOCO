package com.calendarCO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients

public class CalendarCoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CalendarCoApplication.class, args);
	}

}
