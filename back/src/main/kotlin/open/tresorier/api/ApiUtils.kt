package open.tresorier.api

import io.javalin.http.Context
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.NoAuthTokenException
import open.tresorier.model.Person
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*

fun sendToAdminMessage(errorId : String) : String {
    return " Send this code to your administrator for details : $errorId"
}

fun getUserFromAuth(ctx: Context): Person {
    var authData = getAuthenticationData(ctx)
    var personId = authData.subject
    var email : String = authData.get("email").toString() ?: "email was absent from jwt"
    var person = ServiceManager.personService.getById(personId)
    person.email = email 
    return person
}

fun getAuthenticationData(ctx: Context) : Claims {
    var response = ctx.header("Authorization")
    if (response != null) {
        var jws = response.replace("Bearer ", "")
        var publicKeyStr = Properties().get(PropertiesEnum.KEYCLOAK_PUBLIC_KEY)
        var keyspec = X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr))
        var key: PublicKey = KeyFactory.getInstance("RSA").generatePublic(keyspec)
        var jwt = Jwts.parser().verifyWith(key).build().parseSignedClaims(jws)
        return jwt.payload
    }
    throw NoAuthTokenException("no Authorization header")
}

inline fun <reified T: Any> getQueryParam(ctx: Context, paramName: String) : T {
    return ctx.queryParamAsClass(paramName, T::class.java).get()
}

inline fun <reified T: Any> getOptionalQueryParam(ctx: Context, paramName: String) : T? {
    return ctx.queryParamAsClass(paramName, T::class.java).allowNullable().get()
}

