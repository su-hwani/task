package com.example.demo

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey
import java.util.UUID

@Service
class JwtService(
    @Value("\${jwt.secret:}") private val secret: String,
    @Value("\${jwt.issuer:demo}") private val issuer: String,
    @Value("\${jwt.expMinutes:60}") private val expMinutes: Long
) {
    private val key: SecretKey by lazy {
        require(secret.isNotBlank()) { "jwt.secret must be configured" }
        Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun generate(userId: String, email: String, role: String): String {
        val now = Instant.now()
        val exp = now.plusSeconds(expMinutes * 60)
        return Jwts.builder()
            .subject(userId)
            .issuer(issuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(exp))
            .claim("email", email)
            .claim("role", role)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }

    fun parse(token: String) = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).payload

    fun extractUserId(tokenOrBearer: String): UUID {
        val token = tokenOrBearer.removePrefix("Bearer ").trim()
        val claims = parse(token)
        return UUID.fromString(claims.subject)
    }
}


