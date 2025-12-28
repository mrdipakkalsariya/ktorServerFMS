package com.example

/**
 * Generates JWT token after successful login.
 * Token can be used later for authenticated API calls.
 */
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import java.util.*

object JwtConfig {
    private const val secret = "food_colony_secure_secret_key"

    fun generateToken(shopkeeperId: String): String {
        return Jwts.builder()
            .setSubject(shopkeeperId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 86400000)) // 1 day expiry
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }
}
