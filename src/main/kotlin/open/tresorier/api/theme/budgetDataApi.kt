package open.tresorier.api.theme

import io.javalin.Javalin
import io.javalin.http.Context
import io.supertokens.javalin.SuperTokens
import io.supertokens.javalin.core.exception.SuperTokensException
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.exception.SuspendedUserException
import open.tresorier.model.*
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.services.*
import open.tresorier.api.*

fun addBudgetDataRoute(app : Javalin, budgetService: BudgetService, budgetDataService: BudgetDataService) : Javalin {

    app.before("/budget/data", SuperTokens.middleware())
    app.get("/budget/data") { ctx ->
        // required
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        // optional
        val startMonth: Month? = getOptionalQueryParam<Int>(ctx, "start_month")?.let { Month.createFromComparable(it)}
        val endMonth: Month? = getOptionalQueryParam<Int>(ctx, "end_month")?.let { Month.createFromComparable(it)}
        val budgetData = budgetDataService.getBudgetData(user, budget, startMonth, endMonth)
        ctx.json(budgetData)
    }

    app.before("/budget/amount", SuperTokens.middleware())
    app.get("/budget/amount") { ctx ->
        // required
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        // optional
        val month: Month? = getOptionalQueryParam<Int>(ctx, "month")?.let { Month.createFromComparable(it)}
        val budgetData = budgetDataService.getBudgetAmount(user, budget, month)
        ctx.json(budgetData)
    }
    
    return app
}