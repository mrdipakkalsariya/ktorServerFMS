/**
 * Authentication data models for Shopkeeper registration & login
 */
package com.example.models

import kotlinx.serialization.Serializable
@Serializable
data class RegisterRequest(
    val fullName: String,
    val residentAddress: String,
    val shopNameAddress: String,
    val mobile: String,
    val alternateMobile: String,
    val email: String,
    val password: String,
    val confirmPassword: String,
    val shopType: String
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
