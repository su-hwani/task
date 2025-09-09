package com.example.demo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID
import org.springframework.dao.DataIntegrityViolationException
import com.example.demo.Thread
import java.time.OffsetDateTime
import org.springframework.data.domain.PageRequest

@Service
class ThreadService(
    private val threadRepository: ThreadRepository,
    private val personRepository: PersonRepository
) {

	@Transactional
	fun getOrCreateActiveThread(userId: UUID): Thread {
		val last = threadRepository.findLatestWithChats(userId, PageRequest.of(0, 1)).firstOrNull()
		if (last == null) {
			return createThreadEntity(userId)
		}
		// 최근 채팅 시간(없으면 스레드 업데이트/생성 시각)과 현재 시간 비교(30분 룰)
		val lastChatTime: OffsetDateTime? = last.chats.maxOfOrNull { it.createdAt ?: OffsetDateTime.MIN }
		val referenceTime = lastChatTime ?: (last.updatedAt ?: last.createdAt)
		val threshold = OffsetDateTime.now().minusMinutes(30)
		return if (referenceTime == null || referenceTime.isBefore(threshold)) {
			createThreadEntity(userId)
		} else last
	}

	private fun createThreadEntity(userId: UUID): Thread {
		val user = personRepository.findById(userId).orElseThrow { NotFoundException("User not found: ${userId.toString()}") }
		return threadRepository.save(Thread(user = user))
	}
}

class NotFoundException(message: String) : RuntimeException(message)