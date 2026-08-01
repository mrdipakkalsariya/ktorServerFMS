/**
 * Shopkeeper Login API 
 * - Login using Shopkeeper ID + Password
 * - Returns JWT token if login successful
 */
package com.example.routes

import com.example.models.LoginRequest
import com.example.models.ApiResponse
import com.example.JwtConfig
import com.example.SecurityUtil
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.routes.Shopkeepers

// ------------ Login API Endpoint ------------ //

fun Application.configureShopkeeperLoginApi() {
    routing {
        post("/auth/shopkeeper/login") {
            try {
                val request = call.receive<LoginRequest>()

                // 1. Fetch shopkeeper record from DB
                val user = transaction {
                    Shopkeepers.select { Shopkeepers.uid eq request.shopkeeperId }.singleOrNull()
                }

                // 2. If user not found
                if (user == null) {
                    call.respond(ApiResponse(false, "Shopkeeper ID not found ❌"))
                    return@post
                }

                val storedPassword = user[Shopkeepers.password] // Hashed password from DB

                // 3. Verify password
                if (!SecurityUtil.verifyPassword(request.password, storedPassword)) {
                    call.respond(ApiResponse(false, "Incorrect Password ❌"))
                    return@post
                }

                // 4. Generate JWT token
                val token = JwtConfig.generateToken(request.shopkeeperId)

                // 5. Return login success
                call.respond(ApiResponse(true, "Login Successful ✅", token))

            } catch (e: Exception) {
                // This prevents HTTP 500 crash
                println("Login API Crash → ${e.message}")
                call.respond(ApiResponse(false, "Server Error ❌"))
            }
        }
    }
}
