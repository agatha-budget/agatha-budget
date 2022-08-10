package open.tresorier.dao


import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject
import open.tresorier.model.enum.ProfileEnum

open class CategoryDaoTest : ITest {

    val categoryDao by inject<ICategoryDao>()
    val budgetDao by inject<IBudgetDao>()
    val masterCategoryDao by inject<IMasterCategoryDao>()

    @Test fun testGetAllCategoriesFromBudget() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id, "null")
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id, "null")
        masterCategoryDao.insert(masterCategoryHouse)

        val categoryList = listOf(
            Category("boat", masterCategoryDream.id),
            Category("harp", masterCategoryDream.id),
            Category("oven", masterCategoryHouse.id)
        )

        for (category in categoryList) {
            categoryDao.insert(category)
        }

        val universalCategoryList = listOf(
            categoryDao.getById(Category.INCOME_ID),
            categoryDao.getById(Category.TRANSFERT_ID)
        )
        val all = universalCategoryList + categoryList
        val foundCategoryList = categoryDao.findByBudget(budget)
        Assertions.assertEquals(all.toString(), foundCategoryList.toString())
    }

    @Test fun testGetAllCategoriesFromEmptyBudget() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val foundCategoryList = categoryDao.findByBudget(budget)
        val universalCategoryList = listOf(
            categoryDao.getById(Category.INCOME_ID),
            categoryDao.getById(Category.TRANSFERT_ID)
        )
        Assertions.assertEquals(universalCategoryList.toString(), foundCategoryList.toString())
    }

    @Test fun testGetAllCategoriesFromUnknownBudget() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        val foundCategoryList = categoryDao.findByBudget(budget)
        val universalCategoryList = listOf(
            categoryDao.getById(Category.INCOME_ID),
            categoryDao.getById(Category.TRANSFERT_ID)
        )
        Assertions.assertEquals(universalCategoryList.toString(), foundCategoryList.toString())
    }

    @Test fun testGetOwnerOfUniversalCategory() {
        val universalCategory = categoryDao.getById(Category.INCOME_ID)
        val owner = categoryDao.getOwner(universalCategory)
        Assertions.assertNull(owner)
    }

    @Test fun archiveAllDependingCategories() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id, "null")
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id, "null")
        masterCategoryDao.insert(masterCategoryHouse)

        val categoryList = listOf(
            Category("boat", masterCategoryDream.id),
            Category("harp", masterCategoryDream.id),
            Category("oven", masterCategoryHouse.id)
        )

        for (category in categoryList) {
            categoryDao.insert(category)
        }

        var dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        for (category in dreamCategoryList) {
            Assertions.assertEquals(false, category.archived)
        }

        categoryDao.updateArchivedStatusByMasterCategory(masterCategoryDream, true)
        dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        for (category in dreamCategoryList) {
            Assertions.assertEquals(true, category.archived)
        }

        val houseCategoryList = categoryDao.findByMasterCategory(masterCategoryHouse)
        for (category in houseCategoryList) {
            Assertions.assertEquals(false, category.archived)
        }
    }

    @Test fun unarchiveAllDependingCategories() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id, "null")
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id, "null")
        masterCategoryDao.insert(masterCategoryHouse)

        val categoryList = listOf(
            Category("boat", masterCategoryDream.id),
            Category("harp", masterCategoryDream.id),
            Category("oven", masterCategoryHouse.id)
        )

        for (category in categoryList) {
            category.archived = true
            categoryDao.insert(category)
        }

        var dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        for (category in dreamCategoryList) {
            Assertions.assertEquals(true, category.archived)
        }

        categoryDao.updateArchivedStatusByMasterCategory(masterCategoryDream, false)
        dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        for (category in dreamCategoryList) {
            Assertions.assertEquals(false, category.archived)
        }

    }

    @Test fun unarchiveAllDependingCategoriesSomeWereNotArchived() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id, "null")
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id, "null")
        masterCategoryDao.insert(masterCategoryHouse)

        val categoryList = listOf(
            Category("boat", masterCategoryDream.id),
            Category("harp", masterCategoryDream.id),
            Category("oven", masterCategoryHouse.id)
        )

        categoryList[1].archived = true

        for (category in categoryList) {
            categoryDao.insert(category)
        }

        val harpCategory = categoryDao.getById(categoryList[1].id)
        Assertions.assertEquals(true, harpCategory.archived)

        categoryDao.updateArchivedStatusByMasterCategory(masterCategoryDream, false)
        val dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        for (category in dreamCategoryList) {
            Assertions.assertEquals(false, category.archived)
        }
    }

    @Test fun archiveAllDependingCategoriesSomeWereAlreadyArchived() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id, "null")
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id, "null")
        masterCategoryDao.insert(masterCategoryHouse)

        val categoryList = listOf(
            Category("boat", masterCategoryDream.id),
            Category("harp", masterCategoryDream.id),
            Category("oven", masterCategoryHouse.id)
        )

        categoryList[1].archived = true

        for (category in categoryList) {
            categoryDao.insert(category)
        }

        val harpCategory = categoryDao.getById(categoryList[2].id)
        Assertions.assertEquals(false, harpCategory.archived)

        categoryDao.updateArchivedStatusByMasterCategory(masterCategoryDream, true)
        val dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        for (category in dreamCategoryList) {
            Assertions.assertEquals(true, category.archived)
        }
    }

    @Test fun archiveAllDependingCategoriesWhenThereIsNone() {
        val budget = Budget("lucie-B1", TestData.person1Id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id, "null")
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id, "null")
        masterCategoryDao.insert(masterCategoryHouse)

        categoryDao.updateArchivedStatusByMasterCategory(masterCategoryDream, true)
        val dreamCategoryList = categoryDao.findByMasterCategory(masterCategoryDream)
        Assertions.assertEquals(0, dreamCategoryList.size)
    }
}
