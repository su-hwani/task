package com.example.demo

import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PersonRepository : JpaRepository<Person, UUID> {
	fun existsByEmail(email: String): Boolean
	fun findByEmail(email: String): Person?
}


