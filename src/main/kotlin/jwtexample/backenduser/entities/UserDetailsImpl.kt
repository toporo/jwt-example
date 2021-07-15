package jwtexample.backenduser.entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(private val user: User) : UserDetails {

    override fun getAuthorities() = mutableListOf<GrantedAuthority>()

    override fun isEnabled() = true

    override fun getUsername() =  user.username

    fun getEmail() = user.email

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = user.password

    fun getRoles() = user.roles

    fun getName() = user.name

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true
}