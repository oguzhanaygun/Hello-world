package com.hb.helloworld;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	private static final String template = "Hello Hepsiburada from %s !";

	@GetMapping("/hello")
	public String greeting(@RequestParam(name = "name", defaultValue = "Oguzhan") String name) {
		return String.format(template, name);
	}
}