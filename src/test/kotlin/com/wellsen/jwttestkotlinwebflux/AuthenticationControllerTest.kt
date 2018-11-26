package com.wellsen.jwttestkotlinwebflux

import com.wellsen.jwttestkotlinwebflux.security.model.AuthRequest
import com.wellsen.jwttestkotlinwebflux.security.model.AuthResponse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AuthenticationControllerTest {

    @Autowired
    private lateinit var client: WebTestClient

    private val requestMap = HashMap<String, AuthRequest>()

    @Before
    fun setup() {
        requestMap["WrongUser"] = AuthRequest("noname", "password")
        requestMap["WrongPassword"] = AuthRequest("user", "userr")
        requestMap["User"] = AuthRequest("user", "user")
        requestMap["Admin"] = AuthRequest("admin", "admin")
    }

    @Test
    fun wrongUser() {
        client.post().uri("/auth")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestMap["WrongUser"]!!))
                .exchange()
                .expectStatus().isNotFound
    }

    @Test
    fun wrongPassword() {
        client.post().uri("/auth")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestMap["WrongPassword"]!!))
                .exchange()
                .expectStatus().isUnauthorized
    }

    @Test
    fun user() {
        client.post().uri("/auth")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestMap["User"]!!))
                .exchange()
                .expectStatus().isOk
                .expectBody(AuthResponse::class.java)
    }

    @Test
    fun admin() {
        client.post().uri("/auth")
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(requestMap["Admin"]!!))
                .exchange()
                .expectStatus().isOk
                .expectBody(AuthResponse::class.java)
    }
}
