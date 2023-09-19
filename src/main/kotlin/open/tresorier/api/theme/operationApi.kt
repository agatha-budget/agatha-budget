package open.tresorier.api.theme

import io.javalin.Javalin
import io.javalin.http.Context
import open.tresorier.exception.TresorierException
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.exception.SuspendedUserException
import open.tresorier.model.*
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import open.tresorier.services.*
import open.tresorier.api.*

fun addOperationRoute(app : Javalin, accountService: AccountService, budgetService: BudgetService, categoryService: CategoryService, operationService: OperationService) : Javalin {

    app.post("/operation") { ctx ->
        //required
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val day : Day = getQueryParam<Int>(ctx, "day").let {Day.createFromComparable(it)}

        //optional
        val category: Category? = getOptionalQueryParam<String>(ctx, "category_id")?.let{
            categoryService.getById(user, it)
        }
        val amount : Int? = getOptionalQueryParam<Int>(ctx, "amount")
        val memo : String? = getOptionalQueryParam<String>(ctx, "memo")
        val pending : Boolean? = getOptionalQueryParam<Boolean>(ctx, "pending")
        val motherOperation : Operation? = getOptionalQueryParam<String>(ctx, "mother_operation_id")?.let{
            operationService.getById(user, it)
        }
        val operation: Operation = operationService.create(user, account, day, category, amount, memo, pending, motherOperation)
        ctx.json(operation)
    }

    app.put("/operation") { ctx ->
        //required
        val user = getUserFromAuth(ctx)
        val operation: Operation = operationService.getById(user, getQueryParam<String>(ctx, "operation_id"))
    
        //optional
        val account: Account? = getOptionalQueryParam<String>(ctx, "new_account_id")?.let{
            accountService.getById(user, it)
        }
        val day : Day? = getOptionalQueryParam<Int>(ctx, "new_day")?.let {Day.createFromComparable(it)}

        val category: Category? = getOptionalQueryParam<String>(ctx, "new_category_id")?.let{
            categoryService.getById(user, it)
        }
        val removeCategory: Boolean? = getOptionalQueryParam<Boolean>(ctx, "remove_category")
        val amount : Int? = getOptionalQueryParam<Int>(ctx, "new_amount")
        val memo : String? = getOptionalQueryParam<String>(ctx, "new_memo")
        val pending : Boolean? = getOptionalQueryParam<Boolean>(ctx, "new_pending")
        val motherOperationId : String? = getOptionalQueryParam<String>(ctx, "new_mother_operation_id")

        val updatedOperation = operationService.update(user, operation, account, day, category, removeCategory, amount, memo, pending, motherOperationId)
        ctx.json(updatedOperation)
    }

    app.delete("/operation") { ctx ->
        val user = getUserFromAuth(ctx)
        val operation: Operation = operationService.getById(user, getQueryParam<String>(ctx, "operation_id"))
        operationService.delete(user, operation)
        ctx.result("account ${operation.id} has been deleted")
    }

    app.get("/operation/account") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val categoryId: String? = getOptionalQueryParam<String>(ctx, "category_id") 
        val category = categoryId?.let { categoryService.getById(user, it) }
        val operations = operationService.findByAccount(user, account, category)
        ctx.json(operations)
    }

    app.get("/operation/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val categoryId: String? = getOptionalQueryParam<String>(ctx, "category_id")
        val  category = categoryId?.let { categoryService.getById(user, it) }
        val operations = operationService.findByBudget(user, budget, category)
        ctx.json(operations)
    }
 
    app.post("/operation/import") { ctx ->
        val user = getUserFromAuth(ctx)
        val account: Account = accountService.getById(user, getQueryParam<String>(ctx, "account_id"))
        val fileOfx: String = ctx.body()
        val numberOperation = operationService.importOfxFile(user, account, fileOfx)
        ctx.json(numberOperation)
    }
    
    return app
}