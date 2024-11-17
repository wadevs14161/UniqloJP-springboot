package com.example.restservice.greeting;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;



@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@PostMapping("/testPost")
	public void testPost(@RequestBody String name) {
		System.out.println("This is postmapping in GreetingController!");
	}

}
