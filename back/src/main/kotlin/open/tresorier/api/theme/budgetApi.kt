package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.model.Budget
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.services.BudgetService

fun addBudgetRoute(app : Javalin, budgetService: BudgetService) : Javalin {

    app.get("/budget/user") { ctx ->
        val person = getUserFromAuth(ctx)
        val budgetList = budgetService.findByUser(person)
        ctx.json(budgetList)
    }

    app.post("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val name = getQueryParam<String>(ctx, "name")
        val profileString = getQueryParam<String>(ctx, "profile")
        val profile: ProfileEnum = ProfileEnum.valueOf(profileString)
        val budget: Budget = budgetService.create(user, name, profile)
        ctx.result(budget.id)
    }

    app.put("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val formerName = budget.name
        val newName = getQueryParam<String>(ctx, "new_name")
        budgetService.update(user, budget, newName)
        ctx.result("updated from $formerName to $newName")
    }

    app.delete("/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        budgetService.delete(user, budget)
        ctx.result("budget ${budget.name} has been deleted")
    }
    return app
}