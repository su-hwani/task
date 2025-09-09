package com.example.demo

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.security.crypto.password.PasswordEncoder

@Service
class SignUpService(
	private val personRepository: PersonRepository,
	private val passwordEncoder: PasswordEncoder,
	private val jwtService: JwtService
) {

	@Transactional
	fun signUp(req: SignUpRequest): SignUpResponse {
		if (personRepository.existsByEmail(req.email)) {
			throw EmailAlreadyUsedException(message = "Email already used")
		}
		val saved = personRepository.save(
			Person(
				email = req.email.trim(),
				password = passwordEncoder.encode(req.password),
				name = req.name.trim(),
				role = Role.MEMBER
			)
		)
		return SignUpResponse(
			id = saved.id.toString(),
			email = saved.email,
			name = saved.name,
			role = saved.role
		)
	}

	@Transactional(readOnly = true)
	fun login(req: LoginRequest): LoginResponse {
		val user = personRepository.findByEmail(req.email.trim())
			?: throw UnauthorizedException(message = "Unauthorized")
		if (!passwordEncoder.matches(req.password, user.password)) {
			throw UnauthorizedException(message = "Unauthorized")
		}
		val token = jwtService.generate(user.id.toString(), user.email, user.role.name)
		return LoginResponse(
			id = user.id.toString(),
			email = user.email,
			name = user.name,
			role = user.role,
			token = token
		)
	}
}

class EmailAlreadyUsedException(message: String) : RuntimeException(message)
class UnauthorizedException(message: String) : RuntimeException(message)


