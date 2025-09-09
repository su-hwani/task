package com.example.demo

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "thread")
data class Thread(
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	var id: UUID? = null,

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	val user: Person,

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
	var createdAt: OffsetDateTime? = null,

	@UpdateTimestamp
	@Column(name = "updated_at", columnDefinition = "timestamptz", nullable = false)
	var updatedAt: OffsetDateTime? = null,

	@OneToMany(mappedBy = "thread", cascade = [CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE], orphanRemoval = true)
	val chats: MutableList<Chat> = mutableListOf()
)
