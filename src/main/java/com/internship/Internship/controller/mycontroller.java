package com.internship.Internship.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class mycontroller {
	
	@GetMapping("/api")
	public String Greeting() {
		return "Hello, Welcome to the Internship Project";
	}

}
