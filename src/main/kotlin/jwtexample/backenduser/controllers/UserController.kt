package jwtexample.backenduser.controllers

import jwtexample.backenduser.requests.CreateUserRequest
import jwtexample.backenduser.responses.UserResponse
import jwtexample.backenduser.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("user")
class UserController {

    @Autowired
    lateinit var userService: UserService

    @PostMapping
    fun CreateUser(@RequestBody request: CreateUserRequest) : ResponseEntity<UserResponse> {
        val user = userService.createuser(request)
        return ResponseEntity(user, HttpStatus.CREATED)
    }

    @GetMapping("me")
    fun me() = ResponseEntity.ok(userService.myself()!!)
}