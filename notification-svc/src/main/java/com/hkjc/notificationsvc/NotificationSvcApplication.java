package com.hkjc.notificationsvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
@EnableAutoConfiguration(exclude = {FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})
public class NotificationSvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(NotificationSvcApplication.class, args);
	}

}
