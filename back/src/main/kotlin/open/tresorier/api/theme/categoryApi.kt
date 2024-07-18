package open.tresorier.api.theme

import io.javalin.Javalin
import open.tresorier.api.getOptionalQueryParam
import open.tresorier.api.getQueryParam
import open.tresorier.api.getUserFromAuth
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.model.Month
import open.tresorier.services.AllocationService
import open.tresorier.services.BudgetService
import open.tresorier.services.MasterCategoryService
import open.tresorier.services.CategoryService

fun addCategoryRoute(app : Javalin, budgetService: BudgetService, masterCategoryService: MasterCategoryService,
                     categoryService: CategoryService, allocationService: AllocationService) : Javalin {

    app.post("/mcategory") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))
        val name = getQueryParam<String>(ctx, "name")

        val mcategory = masterCategoryService.create(user, budget, name)
        ctx.json(mcategory)
    }

    app.put("/mcategory") { ctx ->
        val user = getUserFromAuth(ctx)
        val masterCategory: MasterCategory = masterCategoryService.getById(user, getQueryParam<String>(ctx, "id"))

        //optional
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newColor = getOptionalQueryParam<String>(ctx, "new_color")
        val newArchived = getOptionalQueryParam<Boolean>(ctx, "new_archived")
        val newDeleted = getOptionalQueryParam<Boolean>(ctx, "new_deleted")

        val updatedMasterCategory = masterCategoryService.update(user, masterCategory, newName, newColor, newArchived, newDeleted)
        ctx.json(updatedMasterCategory)
    }

    app.get("/mcategory/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))

        val masterCategories = masterCategoryService.findByBudget(user, budget)
        ctx.json(masterCategories)
    }

    app.post("/category") { ctx ->
        val user = getUserFromAuth(ctx)
        val masterCategory: MasterCategory = masterCategoryService.getById(user, getQueryParam<String>(ctx, "master_category_id"))
        val name = getQueryParam<String>(ctx, "name")

        val category = categoryService.create(user, masterCategory, name)
        ctx.json(category)
    }

    app.put("/category") { ctx ->
        // required
        val user = getUserFromAuth(ctx)
        val category: Category = categoryService.getById(user, getQueryParam<String>(ctx, "id"))

        //optional
        val newName = getOptionalQueryParam<String>(ctx, "new_name")
        val newMasterCategory: MasterCategory? = getOptionalQueryParam<String>(ctx, "new_master_category_id")?.let{
            masterCategoryService.getById(user, it)
        }
        val newArchived = getOptionalQueryParam<Boolean>(ctx, "new_archived")
        val newDeleted = getOptionalQueryParam<Boolean>(ctx, "new_deleted")
        val updatedCategory = categoryService.update(user, category, newName, newMasterCategory, newArchived, newDeleted)
        ctx.json(updatedCategory)
    }

    app.get("/category/budget") { ctx ->
        val user = getUserFromAuth(ctx)
        val budget: Budget = budgetService.getById(user, getQueryParam<String>(ctx, "budget_id"))

        val categories = categoryService.findByBudget(user, budget)
        ctx.json(categories)
    }

    app.post("/allocation") { ctx ->
        val user = getUserFromAuth(ctx)
        val month : Month = Month.createFromComparable(getQueryParam<Int>(ctx, "month"))
        val category: Category = categoryService.getById(user, getQueryParam<String>(ctx, "category_id"))
        val amount : Int = getQueryParam<Int>(ctx, "amount")
        val allocation = allocationService.insertOrUpdate(user, month, category, amount)
        ctx.json(allocation)
    }

    return app
}