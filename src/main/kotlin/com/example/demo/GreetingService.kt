package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Service
class GreetingService(
	private val personRepository: PersonRepository
) {
	companion object {
		private val logger = LoggerFactory.getLogger(GreetingService::class.java)
	}

	@Value("\${openai.api.key:}")
	private var configuredOpenAiApiKey: String? = null

	private val restTemplate: RestTemplate = RestTemplate()
	private val openAiEndpoint = "https://api.openai.com/v1/chat/completions"

	fun buildGreeting(name: String): GreetingResponse {
		logger.info("name: {}", name)
		val normalizedName = if (name.isBlank()) "World" else name.trim()
		return GreetingResponse(message = "Hello, $normalizedName!")
	}

	fun createPerson(name: String): Person {
		val trimmed = name.trim()
		require(trimmed.isNotEmpty()) { "name must not be blank" }
		return personRepository.save(Person(name = trimmed))
	}

	fun askGptFamousPerson(name: String): String {
		val queryName = name.trim()
		require(queryName.isNotEmpty()) { "name must not be blank" }

		val apiKey = resolveOpenAiApiKey()
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		headers.setBearerAuth(apiKey)

		val prompt = "${queryName} 중 유명한 사람을 알려줘"
		val requestBody = ChatCompletionRequest(
			model = "gpt-4o-mini",
			messages = listOf(
				ChatMessage(role = "system", content = "You are a helpful assistant."),
				ChatMessage(role = "user", content = prompt)
			),
			temperature = 0.2
		)

		val httpEntity = HttpEntity(requestBody, headers)
		return try {
			val response = restTemplate.postForObject(
				openAiEndpoint,
				httpEntity,
				ChatCompletionResponse::class.java
			)
			val answer = response?.choices?.firstOrNull()?.message?.content?.trim()
				?: ""
			if (answer.isBlank()) "응답이 비어있어요." else answer
		} catch (ex: RestClientException) {
			logger.error("OpenAI API 호출 실패", ex)
			"OpenAI API 호출 실패"
		}
	}

	private fun resolveOpenAiApiKey(): String {
		val keyFromEnv = System.getenv("OPENAI_API_KEY")?.trim().orEmpty()
		val keyFromProp = configuredOpenAiApiKey?.trim().orEmpty()
		val key = when {
			keyFromProp.isNotEmpty() -> keyFromProp
			keyFromEnv.isNotEmpty() -> keyFromEnv
			else -> ""
		}
		require(key.isNotEmpty()) { "OpenAI API key not configured. Set openai.api.key or OPENAI_API_KEY." }
		return key
	}

	data class ChatCompletionRequest(
		val model: String,
		val messages: List<ChatMessage>,
		val temperature: Double? = null,
		val max_tokens: Int? = null
	)

	data class ChatMessage(
		val role: String,
		val content: String
	)

	data class ChatCompletionResponse(
		val id: String?,
		val choices: List<Choice>
	) {
		data class Choice(
			val index: Int?,
			val message: ChatMessageContent
		)

		data class ChatMessageContent(
			val role: String,
			val content: String
		)
	}
}