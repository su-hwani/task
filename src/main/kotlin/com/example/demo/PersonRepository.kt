package com.example.demo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.UUID

interface PersonRepository : JpaRepository<Person, UUID> {
	// 파생 쿼리 메서드 (프로퍼티명 기반)
	fun findByName(name: String): List<Person>
	fun findFirstByName(name: String): Person?
	fun existsByName(name: String): Boolean

	// 대소문자 무시 + 부분검색
	fun findByNameContainingIgnoreCase(name: String): List<Person>
	fun findByNameContainingIgnoreCase(name: String, pageable: Pageable): Page<Person>

	// @Query 예시 (JPQL)
	@Query("select p from Person p where lower(p.name) like lower(concat('%', :name, '%'))")
	fun searchByNameLike(@Param("name") name: String): List<Person>
}


