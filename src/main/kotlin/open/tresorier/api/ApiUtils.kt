package open.tresorier.api

import io.javalin.http.Context
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.model.Person

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

