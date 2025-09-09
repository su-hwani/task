package com.example.demo

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController		
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.server.ResponseStatusException
import org.springframework.http.HttpStatus
import java.util.UUID

@RestController
@RequestMapping("/api/chatbot")
class ChatBotController(
	private val chatBotService: ChatBotService,
	private val jwtService: JwtService
) {
	@PostMapping("/gpt")
	fun getGpt(
		@RequestHeader(name = "Authorization", required = false) authorization: String?,
		@RequestBody request: ChatBotRequest
	): ChatBotResponse {
		val token = authorization ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing Authorization header")
		val userId = try { jwtService.extractUserId(token) } catch (e: Exception) {
			throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token")
		}
		val answer = chatBotService.askGpt(userId, request.question)
		return ChatBotResponse(answer = answer)
	}
}


