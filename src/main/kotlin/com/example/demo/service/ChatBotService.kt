package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.client.RestTemplate
import java.util.UUID


@Service
class ChatBotService(
    private val threadService: ThreadService,
    private val chatRepository: ChatRepository
) {

	companion object {
		private val logger = LoggerFactory.getLogger(ChatBotService::class.java)
	}

	@Value("\${openai.api.key:}")
	private var configuredOpenAiApiKey: String? = null
	private val restTemplate: RestTemplate = RestTemplate()
	private val openAiEndpoint = "https://api.openai.com/v1/chat/completions"


	fun askGpt(userId: UUID, question: String): String {
		val queryQuestion = question.trim()

		val apiKey = configuredOpenAiApiKey?.trim().orEmpty()
		require(apiKey.isNotEmpty()) { "OpenAI API key not configured. Set openai.api.key or OPENAI_API_KEY." }
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		headers.setBearerAuth(apiKey)

		// 활성 스레드를 가져오거나(최근 대화 30분 이내), 없으면 생성
		val activeThread = threadService.getOrCreateActiveThread(userId)
		val historyMessages: MutableList<ChatMessage> = mutableListOf()
		activeThread.chats.forEach { chat ->
			historyMessages += ChatMessage(role = "user", content = chat.question)
			historyMessages += ChatMessage(role = "assistant", content = chat.answer)
		}

		val messages = historyMessages + listOf(
			ChatMessage(role = "system", content = "You are a helpful assistant."),
			ChatMessage(role = "user", content = queryQuestion)
		)

		val requestBody = ChatCompletionRequest(
			model = "gpt-4o-mini",
			messages = messages,
			temperature = 0.2
		)

		val httpEntity = HttpEntity(requestBody, headers)
		return try {
			val response = restTemplate.postForObject<ChatCompletionResponse>(
				openAiEndpoint,
				httpEntity,
				ChatCompletionResponse::class.java
			)
			val answer = response?.choices?.firstOrNull()?.message?.content?.trim()
				?: ""
			logger.info("chatbot answer: {}", answer)
			// 대화 저장
			saveChat(activeThread, queryQuestion, answer)
			answer
		}  catch (ex: Exception) {
			logger.error("Unexpected error during GPT call", ex)
			throw CanNotAccessOpenAiException(message = "Can not access OpenAI")
		}
	}

	private fun saveChat(thread: Thread, question: String, answer: String) {
		chatRepository.save(Chat(thread = thread, question = question, answer = answer))
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

class CanNotAccessOpenAiException(message: String) : RuntimeException(message)