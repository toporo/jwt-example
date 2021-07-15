package jwtexample.backenduser

import org.springframework.stereotype.Component
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

@Component
class HashUtils {

    fun sha256(input: String) = hashString("SHA-256", input)

    private fun hashString(type: String, input: String): String {
        val bytes = MessageDigest
            .getInstance(type)
            .digest(input.toByteArray())
        return DatatypeConverter.printHexBinary(bytes).uppercase();
    }
}