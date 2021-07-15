package jwtexample.backenduser.security

import jwtexample.backenduser.entities.UserDetailsImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*


@Component
class JWTUtil {

    @Value("\${jwt.secret}")
    private lateinit var JWT_SECRET: String

    fun generateToken(user: UserDetailsImpl): String {
        return Jwts.builder()
            .setSubject(user.username)
            .setClaims(resolveClaims(user))
            .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
            .setExpiration(Date(System.currentTimeMillis() + 5 * 60 * 1000))
            .compact()
    }

    fun isTokenValid(token: String): Boolean {
        val claims = getClaimsToken(token)
        println(claims)
        if (claims != null) {
            val username = claims.subject
            val expirationDate = claims.expiration
            val now = Date(System.currentTimeMillis())
            if (username != null && expirationDate != null && now.before(expirationDate)) {
                return true
            }
        }
        return false
    }


    private fun getClaimsToken(token: String): Claims? {
        return try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).body;
        } catch (e: Exception) {
            null
        }
    }

    fun getUserName(token: String): String? {
        val claims = getClaimsToken(token)
        return claims?.subject
    }

    private fun resolveClaims(user : UserDetailsImpl): MutableMap<String, Any>{
        var claims: MutableMap<String, Any> =  HashMap()
        var userRoles: List<String> = listOf()

        for (role in user.getRoles()!!) {
            userRoles += role
        }

        claims.put("roles", userRoles)
        claims.put("name", user.getName())
        claims.put("username", user.username)
        claims.put("email", user.getEmail())

        return claims
    }
}