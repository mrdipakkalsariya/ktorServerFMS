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
import org.jetbrains.exposed.sql.transactions.transaction

// Only required imports to resolve datetime/javatime/count issues
import org.jetbrains.exposed.sql.javatime.datetime
//import org.jetbrains.exposed.sql.javatime.JavaLocalDateTime

// ------------ PostgreSQL Table Mapping ------------ //

/**
 * Maps 'shopkeepers' table from PostgreSQL database
 */
object Shopkeepers : Table("shopkeepers") {
    val id = integer("id").autoIncrement() // Primary key (auto increases)
    val uid = varchar("shopkeeper_uid", 20).uniqueIndex() // Generated SK ID
    val fullName = varchar("full_name", 100)
    val mobile = varchar("mobile", 15).uniqueIndex()
    val email = varchar("email", 100).uniqueIndex()
    val shopAddress = text("shop_address")
    val houseAddress = text("house_address")
    val password = text("password")

    //    val createdAt = datetime("created_at").clientDefault { org.jetbrains.exposed.sql.javatime.JavaLocalDateTime.now() }
    val createdAt =
        datetime("created_at") // Default value already handled in DB, so we keep it simple

    override val primaryKey = PrimaryKey(id)
}

// ------------ Registration API Endpoint ------------ //

fun Application.configureShopkeeperRegisterApi() {
    routing {
        post("/auth/shopkeeper/register") {
            val request = call.receive<RegisterRequest>()

            // 1. Check password match
            if (request.password != request.confirmPassword) {
                call.respond(ApiResponse(false, "Password & Confirm Password not matched"))
                return@post
            }

            // 2. Validate mobile number (10 digits)
            if (!request.mobile.matches("^[0-9]{10}$".toRegex())) {
                call.respond(ApiResponse(false, "Invalid Mobile Number"))
                return@post
            }

            // 3. Validate email format
            if (!request.email.contains("@")) {
                call.respond(ApiResponse(false, "Invalid Email Format"))
                return@post
            }

            // 4. Check if user already exists
//            val exists = transaction {
//                Shopkeepers.select {
//                    (Shopkeepers.mobile eq request.mobile) or (Shopkeepers.email eq request.email)
//                }.count()
//            } > 0
            val exists = transaction {
                Shopkeepers.select {
                    (Shopkeepers.mobile eq request.mobile) or (Shopkeepers.email eq request.email)
                }.count() // this now resolves because we imported datetime + javatime
            } > 0

            if (exists) {
                call.respond(ApiResponse(false, "User already registered"))
                return@post
            }

            // 5. Generate Shopkeeper ID (SK10001, SK10002...)
            val newShopkeeperId = transaction {
                val totalUsers = Shopkeepers.selectAll().count().toInt()
                "SK${10000 + totalUsers + 1}"
            }

            // 6. Encrypt password
            val hashedPassword = SecurityUtil.hashPassword(request.password)

            // 7. Insert into DB
            transaction {
                Shopkeepers.insert {
                    it[uid] = newShopkeeperId
                    it[fullName] = request.fullName
                    it[mobile] = request.mobile
                    it[email] = request.email
                    it[shopAddress] = request.shopAddress
                    it[houseAddress] = request.houseAddress
                    it[password] = hashedPassword
                }
            }

            // 8. Send response with generated ID
            call.respond(ApiResponse(true, "Registration Successful", newShopkeeperId))
        }
    }
}
