package com.its.email.example.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author tuannx - itshare
 *
 */
@EnableAutoConfiguration
@ComponentScan("com.its.email.example.*")
@SpringBootApplication
public class ITSEmailExampleBootApp {
	public static void main(String[] args) {
		SpringApplication.run(ITSEmailExampleBootApp.class, "");
	}
}
