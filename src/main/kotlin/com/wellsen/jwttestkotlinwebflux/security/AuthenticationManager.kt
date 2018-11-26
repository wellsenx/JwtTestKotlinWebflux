package com.wellsen.jwttestkotlinwebflux.security

import com.wellsen.jwttestkotlinwebflux.security.model.Role
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class AuthenticationManager : ReactiveAuthenticationManager {

    @Autowired
    private val jwtUtil: JWTUtil? = null

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()

        val username: String?
        username = try {
            jwtUtil!!.getUsernameFromToken(authToken)
        } catch (e: Exception) {
            null
        }

        return if (username != null && jwtUtil!!.validateToken(authToken)) {
            val claims = jwtUtil.getAllClaimsFromToken(authToken)
            val rolesMap = claims.get("role", MutableList::class.java)
            val roles = ArrayList<Role>()
            for (rolemap in rolesMap) {
                if (rolemap is String) {
                    roles.add(Role.valueOf(rolemap))
                }
            }
            val auth = UsernamePasswordAuthenticationToken(
                    username, null,
                    roles.stream()
                            .map { SimpleGrantedAuthority(it.name) }
                            .collect(Collectors.toList())
            )
            Mono.just(auth)
        } else {
            Mono.empty()
        }
    }

}