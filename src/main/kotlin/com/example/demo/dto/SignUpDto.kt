package com.example.demo

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SignUpRequest(
	@field:Email
	@field:NotBlank
	val email: String,

	@field:NotBlank
	@field:Size(min = 8, max = 100)
	val password: String,

	@field:NotBlank
	val name: String
)

data class SignUpResponse(
	val id: String,
	val email: String,
	val name: String,
	val role: Role
)

data class LoginRequest(
	@field:Email
	@field:NotBlank
	val email: String,

	@field:NotBlank
	val password: String
)

data class LoginResponse(
	val id: String,
	val email: String,
	val name: String,
	val role: Role,
	val token: String
)


