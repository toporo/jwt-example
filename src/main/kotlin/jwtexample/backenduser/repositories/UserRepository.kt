package jwtexample.backenduser.repositories

import jwtexample.backenduser.entities.User
import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository : MongoRepository<User, String> {
    fun findOneById(id: ObjectId): User
    fun findOneByEmail(email: String?): User
    fun findOneByUsername(username: String?): User
    override fun deleteAll()
}