package com.wellsen.jwttestkotlinwebflux.controller

import com.wellsen.jwttestkotlinwebflux.model.Message
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ResourceController {

    @RequestMapping(value = ["resource/user"], method = [RequestMethod.GET])
    @PreAuthorize("hasRole('USER')")
    fun user(): Mono<ResponseEntity<*>> {
        return Mono.just(ResponseEntity.ok<Any>(Message("Content for user")))
    }

    @RequestMapping(value = ["resource/admin"], method = [RequestMethod.GET])
    @PreAuthorize("hasRole('ADMIN')")
    fun admin(): Mono<ResponseEntity<*>> {
        return Mono.just(ResponseEntity.ok<Any>(Message("Content for admin")))
    }

    @RequestMapping(value = ["resource/user-or-admin"], method = [RequestMethod.GET])
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun userOrAdmin(): Mono<ResponseEntity<*>> {
        return Mono.just(ResponseEntity.ok<Any>(Message("Content for user or admin")))
    }

}