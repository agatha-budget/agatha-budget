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
import open.tresorier.services.*
import open.tresorier.api.*

fun addOperationRoute(app : Javalin, accountService: AccountService, budgetService: BudgetService, categoryService: CategoryService, operationService: OperationService) : Javalin {

    app.before("/operation", SuperTokens.middleware())
    app.post("/operation") { ctx ->
        //required
        val user = getUserFromAuth(ctx)
        val account: Account = ServiceManager.accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val day : Day = getQueryParam<Int>(ctx, "day").let {Day.createFromComparable(it)}

        //optional
        val category: Category? = getOptionalQueryParam<String>(ctx, "category_id")?.let{
            ServiceManager.categoryService.getById(user, it)
        }
        val amount : Int? = getOptionalQueryParam<Int>(ctx, "amount")
        val memo : String? = getOptionalQueryParam<String>(ctx, "memo")
        val pending : Boolean? = getOptionalQueryParam<Boolean>(ctx, "pending")
        val motherOperation : Operation? = getOptionalQueryParam<String>(ctx, "mother_operation_id")?.let{
            ServiceManager.operationService.getById(user, it)
        }
        val operation: Operation = ServiceManager.operationService.create(user, account, day, category, amount, memo, pending, motherOperation)
        ctx.json(operation)
    }

    app.put("/operation") { ctx ->
        //required
        val user = getUserFromAuth(ctx)
        val operation: Operation = ServiceManager.operationService.getById(user, getQueryParam<String>(ctx, "operation_id"))
    
        //optional
        val account: Account? = getOptionalQueryParam<String>(ctx, "new_account_id")?.let{
            ServiceManager.accountService.getById(user, it)
        }
        val day : Day? = getOptionalQueryParam<Int>(ctx, "new_day")?.let {Day.createFromComparable(it)}

        val category: Category? = getOptionalQueryParam<String>(ctx, "new_category_id")?.let{
            ServiceManager.categoryService.getById(user, it)
        }
        val amount : Int? = getOptionalQueryParam<Int>(ctx, "new_amount")
        val memo : String? = getOptionalQueryParam<String>(ctx, "new_memo")
        val pending : Boolean? = getOptionalQueryParam<Boolean>(ctx, "new_pending")
        val motherOperationId : String? = getOptionalQueryParam<String>(ctx, "new_mother_operation_id")

        val updatedOperation = ServiceManager.operationService.update(user, operation, account, day, category, amount, memo, pending, motherOperationId)
        ctx.json(updatedOperation)
    }

    app.delete("/operation") { ctx ->
        val user = getUserFromAuth(ctx)
        val operation: Operation = ServiceManager.operationService.getById(user, getQueryParam<String>(ctx, "operation_id"))
        ServiceManager.operationService.delete(user, operation)
        ctx.result("account ${operation.id} has been deleted")
    }

    app.before("/operation/account", SuperTokens.middleware())
    app.get("/operation/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = ServiceManager.accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val categoryId: String? = getOptionalQueryParam<String>(ctx, "category_id") 
        val category = categoryId?.let { ServiceManager.categoryService.getById(user, it) }
        val operations = ServiceManager.operationService.findByAccount(user, account, category)
        ctx.json(operations)
    }

    app.before("/operation/budget", SuperTokens.middleware())
    app.get("/operation/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = ServiceManager.budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val categoryId: String? = getOptionalQueryParam<String>(ctx, "category_id")
        val  category = categoryId?.let { ServiceManager.categoryService.getById(user, it) }
        val operations = ServiceManager.operationService.findByBudget(user, budget, category)
        ctx.json(operations)
    }
 
    app.before("/operation/import", SuperTokens.middleware())
    app.post("/operation/import") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = ServiceManager.accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val fileOfx: String = ctx.body()
        val numberOperation = ServiceManager.operationService.importOfxFile(user, account, fileOfx)
        ctx.json(numberOperation)
    }
    
    return app
}