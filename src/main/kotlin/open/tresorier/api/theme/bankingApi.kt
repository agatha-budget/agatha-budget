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
import open.tresorier.services.BudgetService
import open.tresorier.services.BankingService
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.model.enum.PriceIdEnum
import open.tresorier.api.*


fun addBankingRoute(app : Javalin, bankingService: BankingService, 
    accountService: AccountService, budgetService: BudgetService) : Javalin {

    app.get("/banks") { ctx ->
        ctx.json(bankingService.getAvailableBanks())
    }

    app.before("/banking", SuperTokens.middleware())
    app.get("/banking") { ctx ->
        val person = getUserFromAuth(ctx)
        val budgetId = getQueryParam<String>(ctx, "budgetId")
        val budget = budgetService.getById(person, budgetId)
        val bankId = getQueryParam<String>(ctx, "bankId")
        ctx.result(bankingService.getLinkForUserAgreement(person, budget, bankId))
    }

    app.put("/banking") { ctx ->
        val person = getUserFromAuth(ctx)
        val bankAgreementId = getQueryParam<String>(ctx, "bankAgreementId")
        val bankAgreement = bankingService.getAgreementById(bankAgreementId)
        bankingService.updateBankAccountList(person, bankAgreement)
    }

    app.before("/bank/accounts", SuperTokens.middleware())
    app.get("/bank/accounts") { ctx ->
        val person = getUserFromAuth(ctx)
        val budgetId = getQueryParam<String>(ctx, "budgetId")
        val budget = budgetService.getById(person, budgetId)
        bankingService.synchronise(person, budget)
    }

    app.before("/bank/operations", SuperTokens.middleware())
    app.get("/bank/operations") { ctx ->
        val person = getUserFromAuth(ctx)
        val accountId = getOptionalQueryParam<String>(ctx, "accountId")
        val budgetId = getOptionalQueryParam<String>(ctx, "budgetId")

        if (accountId != null) {
            val account = accountService.getById(person, accountId)
            bankingService.synchronise(person, account)
        } else if (budgetId != null) {
            val budget = budgetService.getById(person, budgetId)
            bankingService.synchronise(person, budget)
        }
    }

  
    
    return app
}

