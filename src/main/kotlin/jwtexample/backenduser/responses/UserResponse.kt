package jwtexample.backenduser.responses

import jwtexample.backenduser.entities.User

class UserResponse {
    var id : String = ""
    var name : String = ""
    var username : String = ""
    var email : String = ""

    constructor(user : User) {
        id = user.id.toString()
        name = user.name
        username = user.username
        email = user.email
    }
}