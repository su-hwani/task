package com.example.demo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "person")
data class Person(
	@Id
	@GeneratedValue
	@UuidGenerator
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	val id: UUID? = null,

	@Column(name = "name", nullable = false)
	val name: String
)


