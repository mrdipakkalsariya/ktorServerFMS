package com.example

/**
 * Used to hash and verify passwords using BCrypt encryption.
 */
import org.mindrot.jbcrypt.BCrypt

object SecurityUtil {
    fun hashPassword(password: String): String = BCrypt.hashpw(password, BCrypt.gensalt())
    fun verifyPassword(password: String, hashed: String): Boolean = BCrypt.checkpw(password, hashed)
}
