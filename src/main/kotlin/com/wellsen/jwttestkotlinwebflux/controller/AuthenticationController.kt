package com.wellsen.jwttestkotlinwebflux.controller

import com.wellsen.jwttestkotlinwebflux.model.User
import com.wellsen.jwttestkotlinwebflux.security.JWTUtil
import com.wellsen.jwttestkotlinwebflux.security.PBKDF2Encoder
import com.wellsen.jwttestkotlinwebflux.security.model.AuthRequest
import com.wellsen.jwttestkotlinwebflux.security.model.AuthResponse
import com.wellsen.jwttestkotlinwebflux.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
class AuthenticationController {

    @Autowired
    private val jwtUtil: JWTUtil? = null

    @Autowired
    private val passwordEncoder: PBKDF2Encoder? = null

    @Autowired
    private lateinit var userRepository: UserService

    @RequestMapping(value = ["auth"], method = [RequestMethod.POST])
    fun auth(@RequestBody request: AuthRequest): Mono<ResponseEntity<AuthResponse>> {
        if (userRepository.findByUsername(request.username) == Mono.empty<User>()) {
            return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .build())
        }

        return userRepository
                .findByUsername(request.username)
                .map {
                    if (passwordEncoder!!.encode(request.password!!) == it.password) {
                        ResponseEntity.ok(AuthResponse(jwtUtil!!.generateToken(it)))
                    } else {
                        ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                .build()
                    }
                }
    }

}