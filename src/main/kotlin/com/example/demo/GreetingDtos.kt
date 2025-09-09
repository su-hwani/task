package com.example.demo

import jakarta.validation.constraints.NotBlank

data class GreetingRequest(

	@field:NotBlank
	val name: String
)

data class GreetingResponse(
	val message: String
)


