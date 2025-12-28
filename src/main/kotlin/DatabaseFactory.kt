package com.example
/**
 * Initializes PostgreSQL database using Hikari Connection Pool.
 * Exposed ORM will use this connection to run queries safely.
 */
/**
 * This object connects your Ktor server to PostgreSQL database using HikariCP.
 * HikariCP helps handle multiple users efficiently.
 */
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {

    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/food_colony"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "123456" // Your DB password
            maximumPoolSize = 10
            isAutoCommit = false
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        println("Database connected successfully ✅")
    }
}


