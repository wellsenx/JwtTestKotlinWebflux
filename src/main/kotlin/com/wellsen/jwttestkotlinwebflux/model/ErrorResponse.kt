package com.wellsen.jwttestkotlinwebflux.model

data class ErrorResponse(
//        val code: String,
//        val title: String,
        val message: String,
        val errors: List<Error>)