package jwtexample.backenduser.services

import jwtexample.backenduser.entities.UserDetailsImpl
import jwtexample.backenduser.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl: UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = userRepository.findOneByUsername(username) ?: throw UsernameNotFoundException(username)

        return UserDetailsImpl(user)
    }
}