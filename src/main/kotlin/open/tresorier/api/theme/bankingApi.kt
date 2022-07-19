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
import open.tresorier.services.AccountService
import open.tresorier.services.BankingService
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.model.enum.PriceIdEnum
import open.tresorier.api.*


fun addBankingRoute(app : Javalin, personService: PersonService, bankingService: BankingService, accountService: AccountService) : Javalin {

    app.before("/banking/setup", SuperTokens.middleware())
    app.get("/banking/setup") { ctx ->
        val person = getUserFromAuth(ctx)
        val bankId = getQueryParam<String>(ctx, "bankId")
        ctx.result(bankingService.getLinkForUserAgreement(person, bankId))
    }

    app.before("/banking/", SuperTokens.middleware())
    app.put("/banking") { ctx ->
        val person = getUserFromAuth(ctx)
        val bankAgreementId = getQueryParam<String>(ctx, "bankAgreementId")
        val bankAgreement = bankingService.getAgreementById(bankAgreementId)
        bankingService.updateBankAccountList(person, bankAgreement)
    }

    app.get("/banking") { ctx ->
        val person = getUserFromAuth(ctx)
        val accountId = getQueryParam<String>(ctx, "accountId")
        val account = accountService.getById(person, accountId)
        bankingService.synchronise(person, account)
    }
    
    return app
}

