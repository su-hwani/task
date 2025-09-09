package com.example.demo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "chat")
data class Chat(
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	var id: UUID? = null,

	@ManyToOne(optional = false)
	@JoinColumn(name = "thread_id", nullable = false)
	val thread: Thread,

	@Column(name = "question", nullable = false, columnDefinition = "text")
	val question: String,

	@Column(name = "answer", nullable = false, columnDefinition = "text")
	val answer: String,

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
	var createdAt: OffsetDateTime? = null
)


