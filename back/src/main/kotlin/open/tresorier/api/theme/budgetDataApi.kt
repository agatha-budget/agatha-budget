package open.tresorier.api.theme

import io.javalin.router.JavalinDefaultRoutingApi
import open.tresorier.api.getOptionalQueryParam
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.model.Budget
import open.tresorier.model.Month
import open.tresorier.services.BudgetDataService
import open.tresorier.services.BudgetService

fun addBudgetDataRoute(routes: JavalinDefaultRoutingApi, budgetService: BudgetService, budgetDataService: BudgetDataService) {

    routes.get("/budget/data") { ctx ->
        // required
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        // optional
        val startMonth: Month? = getOptionalQueryParam<Int>(ctx, "start_month")?.let { Month.createFromComparable(it)}
        val endMonth: Month? = getOptionalQueryParam<Int>(ctx, "end_month")?.let { Month.createFromComparable(it)}
        val budgetData = budgetDataService.getBudgetData(user, budget, startMonth, endMonth)
        ctx.json(budgetData)
    }

    routes.get("/budget/amount") { ctx ->
        // required
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        // optional
        val month: Month? = getOptionalQueryParam<Int>(ctx, "month")?.let { Month.createFromComparable(it)}
        val budgetData = budgetDataService.getBudgetAmount(user, budget, month)
        ctx.json(budgetData)
    }
    

}