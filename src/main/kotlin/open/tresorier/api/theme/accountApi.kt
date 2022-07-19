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
import open.tresorier.services.AccountService
import open.tresorier.services.BudgetService
import open.tresorier.api.*

fun addAccountRoute(app : Javalin, accountService: AccountService, budgetService: BudgetService) : Javalin {

    app.before("/account", SuperTokens.middleware())
    app.post("/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val name = getQueryParam<String>(ctx, "name")
        val amount = getQueryParam<Int>(ctx, "amount")
        val day = Day.createFromComparable(getQueryParam<Int>(ctx, "day"))
        val account = accountService.create(user, budget, name, day, amount)
        ctx.json(account)
    }

    app.put("/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val name = getOptionalQueryParam<String>(ctx, "name")
        val bankAccountId = getOptionalQueryParam<String>(ctx, "bank_accountId")
        val bankAccount = bankAccountId?.let {accountService.getBankAccountById(user, bankAccountId)}
        accountService.update(user, account, name, bankAccount)
        ctx.result("updated")
    }

    app.delete("/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        accountService.delete(user, account)
        ctx.result("account ${account.name} has been deleted")
    }

    app.before("/account/budget", SuperTokens.middleware())
    app.get("/account/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val accounts = accountService.findByBudget(user, budget)
        ctx.json(accounts)
    }    
    
    return app
}