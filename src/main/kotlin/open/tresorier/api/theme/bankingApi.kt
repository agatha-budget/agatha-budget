package open.tresorier.api.theme

import io.javalin.Javalin
import io.javalin.http.Context
import io.supertokens.javalin.SuperTokens
import io.supertokens.javalin.core.exception.SuperTokensException
import open.tresorier.dependenciesinjection.ServiceManager
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.exception.SuspendedUserException
import open.tresorier.model.*
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.services.PersonService
import open.tresorier.services.BankingService
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.model.enum.PriceIdEnum
import open.tresorier.api.*


fun addBankingRoute(app : Javalin, personService: PersonService, bankingService: BankingService) : Javalin {

    //app.before("/banking/setup", SuperTokens.middleware())
    app.get("/banking/setup") { ctx ->
        val person = personService.getById("person1")
        val bankId = "CREDIT_COOPERATIF_CCOPFRPPXXX" //getQueryParam<String>(ctx, "bankId")
        ctx.result(bankingService.getLinkForUserAgreement(person, bankId))
    }
    
    return app
}

