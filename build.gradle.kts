plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.kotlin.plugin.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "com.example.ApplicationKt"
}

dependencies {
    implementation(libs.ktor.server.auth)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.auth.jwt)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-serialization-kotlinx-json")

    // PostgreSQL + Connection Pool
    implementation("com.zaxxer:HikariCP:5.0.1")
    implementation("org.postgresql:postgresql:42.6.0")

    // ORM for database operations
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
//    implementation("org.jetbrains.exposed:exposed-datetime:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc")
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.jetbrains.exposed:exposed-java-time:0.41.1")


    // Password encryption
    implementation("org.mindrot:jbcrypt:0.4")

    // JWT token
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")
}
