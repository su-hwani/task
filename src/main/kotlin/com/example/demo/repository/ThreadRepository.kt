package com.example.demo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.domain.Pageable
import java.util.UUID

interface ThreadRepository : JpaRepository<Thread, UUID> {
	@Query(
		"""
		select distinct t
		from Thread t
		left join fetch t.chats
		where t.user.id = :userId
		order by t.updatedAt desc
		"""
	)
	fun findLatestWithChats(
		@Param("userId") userId: UUID,
		pageable: Pageable
	): List<Thread>
}


