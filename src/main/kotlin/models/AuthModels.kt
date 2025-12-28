/**
 * Authentication data models for Shopkeeper registration & login
 */
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val fullName: String,
    val mobile: String,
    val email: String,
    val shopAddress: String,
    val houseAddress: String,
    val password: String,
    val confirmPassword: String
)

@Serializable
data class LoginRequest(
    val shopkeeperId: String,
    val password: String
)

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String,
    val data: String? = null
)
