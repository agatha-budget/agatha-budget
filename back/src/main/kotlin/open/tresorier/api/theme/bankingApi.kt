package open.tresorier.api.theme

import io.javalin.router.JavalinDefaultRoutingApi
import open.tresorier.api.getOptionalQueryParam
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.services.AccountService
import open.tresorier.services.BankingService
import open.tresorier.services.BudgetService


fun addBankingRoute(routes: JavalinDefaultRoutingApi, bankingService: BankingService, 
    accountService: AccountService, budgetService: BudgetService) {

    routes.get("/banks") { ctx ->
        ctx.json(bankingService.getAvailableBanks())
    }

    routes.get("/banking") { ctx ->
        val person = getUserFromAuth(ctx)
        val budgetId = getQueryParam<String>(ctx, "budgetId")
        val budget = budgetService.getById(person, budgetId)
        val bankId = getQueryParam<String>(ctx, "bankId")
        ctx.result(bankingService.getLinkForUserAgreement(person, budget, bankId))
    }

    routes.put("/banking") { ctx ->
        val person = getUserFromAuth(ctx)
        val bankAgreementId = getQueryParam<String>(ctx, "bankAgreementId")
        val bankAgreement = bankingService.getAgreementById(bankAgreementId)
        bankingService.updateBankAccountList(person, bankAgreement)
    }

    routes.get("/bank/accounts") { ctx ->
        val person = getUserFromAuth(ctx)
        val budgetId = getQueryParam<String>(ctx, "budgetId")
        val budget = budgetService.getById(person, budgetId)
        ctx.json(bankingService.findBankAccountByBudget(person, budget))
    }

    routes.get("/bank/operations") { ctx ->
        val person = getUserFromAuth(ctx)
        val accountId = getOptionalQueryParam<String>(ctx, "accountId")
        if (accountId != null) {
            val account = accountService.getById(person, accountId)
            bankingService.synchronise(person, account)
        } else {
            bankingService.synchronise(person)
        }
    }

    routes.post("/bank/sync") { _ ->
        bankingService.synchronise()
    }
    

}

