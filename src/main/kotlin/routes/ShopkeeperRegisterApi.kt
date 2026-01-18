/**
 * Shopkeeper Registration API
 * - Validates input
 * - Generates Shopkeeper ID (SK10001...)
 * - Stores shopkeeper data in PostgreSQL
 */
package com.example.routes

import com.example.models.RegisterRequest
import com.example.models.ApiResponse
import com.example.SecurityUtil
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import java.time.LocalDateTime
import org.jetbrains.exposed.sql.transactions.transaction

// Only required imports to resolve datetime/javatime/count issues
import org.jetbrains.exposed.sql.javatime.datetime

//import org.jetbrains.exposed.sql.javatime.JavaLocalDateTime

// ------------ PostgreSQL Table Mapping ------------ //

// ------------ PostgreSQL Table Mapping ------------ //

object Shopkeepers : Table("shopkeepers") {

    val id = integer("id").autoIncrement()
    val uid = varchar("shopkeeper_uid", 20).uniqueIndex()

    val fullName = varchar("full_name", 100)
    val residentAddress = text("resident_address")
    val shopNameAddress = text("shop_name_address")

    val mobile = varchar("mobile", 10).uniqueIndex()
    val alternateMobile = varchar("alternate_mobile", 10)

    val email = varchar("email", 100).uniqueIndex()
    val shopType = varchar("shop_type", 50)

    val password = text("password")
    val createdAt = datetime("created_at")

    override val primaryKey = PrimaryKey(id)
}


// ------------ API Route ------------ //

fun Application.configureShopkeeperRegisterApi() {

    routing {
        post("/auth/shopkeeper/register") {

            try {
                val request = call.receive<RegisterRequest>()

                // 1. Password check
                if (request.password != request.confirmPassword) {
                    call.respond(ApiResponse(false, "Passwords do not match"))
                    return@post
                }

                // 2. Mobile validation
                if (!request.mobile.matches(Regex("^[0-9]{10}$"))) {
                    call.respond(ApiResponse(false, "Invalid mobile number"))
                    return@post
                }

                // 3. Check existing user
                val exists = transaction {
                    Shopkeepers.select {
                        (Shopkeepers.mobile eq request.mobile) or
                                (Shopkeepers.email eq request.email)
                    }.count()
                } > 0

                if (exists) {
                    call.respond(ApiResponse(false, "User already registered"))
                    return@post
                }

                // 4. Generate Shopkeeper ID
                val newId = transaction {
                    val count = Shopkeepers.selectAll().count()
                    "SK${10000 + count + 1}"
                }

                // 5. Hash password
                val hashedPassword = SecurityUtil.hashPassword(request.password)

                // 6. Insert data (FIXED)
                transaction {
                    Shopkeepers.insert {
                        it[uid] = newId
                        it[fullName] = request.fullName
                        it[residentAddress] = request.residentAddress
                        it[shopNameAddress] = request.shopNameAddress
                        it[mobile] = request.mobile
                        it[alternateMobile] = request.alternateMobile
                        it[email] = request.email
                        it[shopType] = request.shopType
                        it[password] = hashedPassword
                        it[createdAt] = LocalDateTime.now() // ✅ REQUIRED
                    }
                }

                call.respond(
                    ApiResponse(true, "Registration successful", newId)
                )

            } catch (e: Exception) {
                e.printStackTrace() // ✅ DEBUG ONLY
                call.respond(ApiResponse(false, "Server error"))
            }
        }
    }
}
