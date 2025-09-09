package com.example.demo

import java.time.OffsetDateTime

data class ThreadResponse(
	val id: String,
	val userId: String,
	val createdAt: OffsetDateTime?,
	val updatedAt: OffsetDateTime?
)


