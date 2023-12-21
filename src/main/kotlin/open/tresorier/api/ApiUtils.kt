package open.tresorier.api

import io.javalin.http.Context
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.model.Person
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import org.pac4j.core.client.Clients
import org.pac4j.oidc.client.KeycloakOidcClient
import org.pac4j.core.config.Config as AuthenticationConfig
import org.pac4j.oidc.config.KeycloakOidcConfiguration
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod
import org.pac4j.core.authorization.authorizer.CsrfAuthorizer
import org.pac4j.core.matching.matcher.CorsMatcher

fun sendToAdminMessage(errorId : String) : String {
    return " Send this code to your administrator for details : $errorId"
}

fun getUserFromAuth(ctx: Context): Person {
    // val validSession = SuperTokens.getFromContext(ctx)
    // val userId = validSession.userId
    val userId = "afwe"
    return ServiceManager.personService.getById(userId)
}

inline fun <reified T: Any> getQueryParam(ctx: Context, paramName: String) : T {
    return ctx.queryParamAsClass(paramName, T::class.java).get()
}

inline fun <reified T: Any> getOptionalQueryParam(ctx: Context, paramName: String) : T? {
    return ctx.queryParamAsClass(paramName, T::class.java).allowNullable().get()
}

fun setUpAuthentication(properties: Properties): AuthenticationConfig {

    val oidcConfig: KeycloakOidcConfiguration = KeycloakOidcConfiguration()
    oidcConfig.setClientId(properties.get(KEYCLOAK_ID))
    oidcConfig.setSecret(properties.get(KEYCLOAK_SECRET))
    oidcConfig.setDiscoveryURI(properties.get(KEYCLOAK_DISC_URI))
    oidcConfig.setBaseUri(properties.get(KEYCLOAK_BASE_URI))
    oidcConfig.setRealm(properties.get(KEYCLOAK_REALM))
    oidcConfig.setClientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)

    val keyCloakClient = KeycloakOidcClient(oidcConfig)
    val clients = Clients(properties.get(API_BASE_URL)+"/callback", keyCloakClient)
    val config = AuthenticationConfig(clients)
    val corsMatcher = CorsMatcher()
    corsMatcher.setAllowOrigin(properties.get(FRONT_URL) + " " + properties.get(KEYCLOAK_BASE_URI))
    config.addMatcher("cors", CorsMatcher())
    return config
}
