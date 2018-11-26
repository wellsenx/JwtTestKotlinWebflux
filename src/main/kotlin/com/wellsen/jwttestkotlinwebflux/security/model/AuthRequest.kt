package com.wellsen.jwttestkotlinwebflux.security.model

data class AuthRequest(
        val username: String?,
        val password: String?)