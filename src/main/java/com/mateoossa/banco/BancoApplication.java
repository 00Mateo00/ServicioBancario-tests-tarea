package com.mateoossa.banco;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BancoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BancoApplication.class, args);

		// Add this line to ensure BuggyClass is compiled and part of the analysis scope
		BuggyClass myBuggyInstance = new BuggyClass();
		myBuggyInstance.getItems(); // Just calling the method
	}

}