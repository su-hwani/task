package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/greet")
class GreetingController(
	private val greetingService: GreetingService
) {
	@GetMapping
	fun getGreet(@RequestParam(required = false, defaultValue = "") name: String): GreetingResponse {
		return greetingService.buildGreeting(name)
	}

	@PostMapping
	fun postGreet(@Valid @RequestBody request: GreetingRequest): GreetingResponse {
		val saved = greetingService.createPerson(request.name)
		return GreetingResponse(message = "saved id=$saved.id name=$saved.name")
	}

	@GetMapping("/gpt")
	fun getGpt(@RequestParam name: String): GreetingResponse {
		val answer = greetingService.askGptFamousPerson(name)
		return GreetingResponse(message = answer)
	}
}


