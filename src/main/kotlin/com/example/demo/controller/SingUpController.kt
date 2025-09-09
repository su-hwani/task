package com.example.demo

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/signup")
class SingUpController(
	private val signUpService: SignUpService
) {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun signUp(@Valid @RequestBody req: SignUpRequest): SignUpResponse {
		return signUpService.signUp(req)
	}

	@PostMapping("/login")
	fun login(@Valid @RequestBody req: LoginRequest): LoginResponse {
		return signUpService.login(req)
	}

	@ResponseStatus(HttpStatus.CONFLICT)
	@ExceptionHandler(EmailAlreadyUsedException::class)
	fun handleDup(): Map<String, String> = mapOf("error" to "EMAIL_ALREADY_USED")

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(UnauthorizedException::class)
	fun handleUnauthorized(): Map<String, String> = mapOf("error" to "UNAUTHORIZED")
}



