package com.example.hhplus.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ConcertReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConcertReservationApplication.class, args);
	}

}
