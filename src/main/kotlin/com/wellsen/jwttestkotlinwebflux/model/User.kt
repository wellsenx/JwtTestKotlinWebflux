package com.wellsen.jwttestkotlinwebflux.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.wellsen.jwttestkotlinwebflux.security.model.Role
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.stream.Collectors

data class User(
        private var username: String,
        private var password: String,
        var enabled: Boolean,
        var roles: List<Role>) : UserDetails {

    override fun getUsername(): String {
        return username
    }

    override fun isAccountNonExpired(): Boolean {
        return false
    }

    override fun isAccountNonLocked(): Boolean {
        return false
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return this.enabled
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.roles.stream()
                .map { SimpleGrantedAuthority(it.name) }
                .collect(Collectors.toList())
    }

    @JsonIgnore
    override fun getPassword(): String? {
        return password
    }

}