package com.example.restservice.greeting;

import java.net.URI;
import java.util.concurrent.atomic.AtomicLong;

import com.linecorp.bot.messaging.model.SetWebhookEndpointRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.linecorp.bot.messaging.client.MessagingApiClient;


@RestController
public class GreetingController {

	private static final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();

	@GetMapping("/greeting")
	public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
		return new Greeting(counter.incrementAndGet(), String.format(template, name));
	}

	@PostMapping("/testPost")
	public String testPost(@RequestBody String name) {
		System.out.println("HERE!");
		return String.format("HEYHEY, %s!", name);
	}

}
