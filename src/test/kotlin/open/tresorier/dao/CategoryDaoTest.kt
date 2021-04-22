package open.tresorier.dao


import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Budget
import open.tresorier.model.Category
import open.tresorier.model.MasterCategory
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

open class CategoryDaoTest : ITest {

    val categoryDao by inject<ICategoryDao>()
    val budgetDao by inject<IBudgetDao>()
    val masterCategoryDao by inject<IMasterCategoryDao>()

    @Test fun testGetAllCategoriesFromBudget() {
        val budget = Budget("lucie-B1", TestData.person1Id)
        budgetDao.insert(budget)
        val masterCategoryDream = MasterCategory("dreams", budget.id)
        masterCategoryDao.insert(masterCategoryDream)
        val masterCategoryHouse = MasterCategory("house", budget.id)
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
        val budget = Budget("lucie-B1", TestData.person1Id)
        budgetDao.insert(budget)
        val foundCategoryList = categoryDao.findByBudget(budget)
        val universalCategoryList = listOf(
            categoryDao.getById(Category.INCOME_ID),
            categoryDao.getById(Category.TRANSFERT_ID)
        )
        Assertions.assertEquals(universalCategoryList.toString(), foundCategoryList.toString())
    }

    @Test fun testGetAllCategoriesFromUnknownBudget() {
        val budget = Budget("lucie-B1", TestData.person1Id)
        val foundCategoryList = categoryDao.findByBudget(budget)
        val universalCategoryList = listOf(
            categoryDao.getById(Category.INCOME_ID),
            categoryDao.getById(Category.TRANSFERT_ID)
        )
        Assertions.assertEquals(universalCategoryList.toString(), foundCategoryList.toString())
    }
}
