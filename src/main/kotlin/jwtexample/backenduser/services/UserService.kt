package jwtexample.backenduser.services

import jwtexample.backenduser.HashUtils
import jwtexample.backenduser.entities.User
import jwtexample.backenduser.entities.UserDetailsImpl
import jwtexample.backenduser.repositories.UserRepository
import jwtexample.backenduser.requests.CreateUserRequest
import jwtexample.backenduser.responses.UserResponse
import org.bson.types.ObjectId
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

@Service
class UserService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var bCryptPasswordEncoder: BCryptPasswordEncoder

    private val EMAIL_IN_USE_EXCEPTION : String = "Email já está em uso"
    private val USERNAME_IN_USE_EXCEPTION : String = "Username já está em uso"

    fun createuser(request : CreateUserRequest) : UserResponse {
        validateEmail(request.email)
        validateUsername(request.username)

        val password = bCryptPasswordEncoder.encode(request.password);

        val roleDefault = listOf<String>("user")

        var user = User(name = request.name, username = request.username, email = request.email, password = password, roles = roleDefault)
        var savedUser = userRepository.save(user)
        return UserResponse(savedUser)
    }

    fun getUser(id : String) : UserResponse {
        var user = userRepository.findOneById(ObjectId(id))
        return UserResponse(user)
    }

    fun myself(): UserResponse? {
        var user = userRepository.findOneByEmail(getCurrentUserEmail())
        return UserResponse(user)
    }

    private fun getCurrentUserEmail(): String? {
        val user = SecurityContextHolder.getContext().authentication.principal as UserDetailsImpl
        return user.username
    }

    private fun validateEmail(email : String) {
        try{
            userRepository.findOneByEmail(email)
        }catch(ex: EmptyResultDataAccessException){
            return
        }
        throw RuntimeException(EMAIL_IN_USE_EXCEPTION);
    }

    private fun validateUsername(username : String) {
        try{
            userRepository.findOneByUsername(username)
        }catch(ex: EmptyResultDataAccessException){
            return
        }
        throw RuntimeException(USERNAME_IN_USE_EXCEPTION);
    }
}