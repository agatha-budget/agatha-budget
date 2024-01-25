package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.api.getOptionalQueryParam
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Day
import open.tresorier.services.AccountService
import open.tresorier.services.BankingService
import open.tresorier.services.BudgetService

fun addAccountRoute(app : Javalin, accountService: AccountService, budgetService: BudgetService, bankingService: BankingService) : Javalin {

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
        name?.let {
            accountService.update(user, account, name)
        }
        ctx.result("updated")
    }

    app.delete("/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        accountService.delete(user, account)
        ctx.result("account ${account.name} has been deleted")
    }

    app.put("/account/bank") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val bankAccountId = getOptionalQueryParam<String>(ctx, "bank_account_id")
        val bankAccount = bankAccountId?.let {bankingService.getBankAccountById(user, bankAccountId)}
        val importHistory = getQueryParam<Boolean>(ctx, "import_history")
        accountService.updateBankAssociation(user, account, bankAccount, importHistory)
        ctx.result("updated")
    }

    app.get("/account/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val accounts = accountService.findByBudget(user, budget)
        ctx.json(accounts)
    }    
    
    return app
}