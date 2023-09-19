package open.tresorier.api

import io.javalin.http.Context
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.model.Person

fun sendToAdminMessage(errorId : String) : String {
    return " Send this code to your administrator for details : $errorId"
}

fun getHerokuAssignedOrDefaultPort(): Int {
    val herokuPort = System.getenv("PORT");
    if (herokuPort != null) {
        return Integer.parseInt(herokuPort);
    }
    return 7000;
}

fun getUserFromAuth(ctx: Context): Person {
    // val validSession = SuperTokens.getFromContext(ctx)
    val userId = validSession.userId
    return ServiceManager.personService.getById(userId)
}

inline fun <reified T: Any> getQueryParam(ctx: Context, paramName: String) : T {
    return ctx.queryParam<T>(paramName).get()
}

inline fun <reified T: Any> getOptionalQueryParam(ctx: Context, paramName: String) : T? {
    val param = ctx.queryParam<T>(paramName).getOrNull()
    return if (param == "null") null else param
}
