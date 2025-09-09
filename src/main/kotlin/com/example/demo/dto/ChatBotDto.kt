package com.example.demo

import jakarta.validation.constraints.NotBlank

data class ChatBotRequest(
	val question: String
)

data class ChatBotResponse(
	val answer: String
)


