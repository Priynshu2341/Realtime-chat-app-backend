package com.example.real_time_messaging_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RealTimeMessagingSystemApplication {


	public static void main(String[] args) {

		java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(RealTimeMessagingSystemApplication.class, args);
	}

}
