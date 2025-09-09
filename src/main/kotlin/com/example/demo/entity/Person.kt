package com.example.demo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UuidGenerator
import java.time.OffsetDateTime
import java.util.UUID

@Entity
@Table(name = "person")
data class Person(
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	var id: UUID? = null,

	@Column(name = "email", nullable = false, unique = true, length = 255)
	val email: String,

	@Column(name = "password", nullable = false, length = 255)
	val password: String,

	@Column(name = "name", nullable = false)
	val name: String,

	@CreationTimestamp
	@Column(name = "created_at", columnDefinition = "timestamptz", nullable = false)
	var createdAt: OffsetDateTime? = null,

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false, length = 20)
	val role: Role = Role.MEMBER
)

enum class Role {
	MEMBER,
	ADMIN
}


