package com.example

import com.example.routes.configureShopkeeperLoginApi
import com.example.routes.configureShopkeeperRegisterApi
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
//    configureSecurity()
//    DatabaseFactory.init() // Connect to PostgreSQL
//    configureSerialization()
//    configureRouting()
    DatabaseFactory.init()
    configureSerialization()
    configureShopkeeperRegisterApi()
    configureShopkeeperLoginApi()
}
