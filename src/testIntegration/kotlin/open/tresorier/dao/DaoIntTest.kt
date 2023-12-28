package open.tresorier.dao

import open.tresorier.dependenciesinjection.IIntegrationTest
import open.tresorier.model.*
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.TestData
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class AccountDaoIntTest : AccountDaoTest(), IIntegrationTest {}

class AllocationDaoIntTest : AllocationDaoTest(), IIntegrationTest {}

class BudgetDaoIntTest : BudgetDaoTest(), IIntegrationTest {}

class CategoryDaoIntTest : CategoryDaoTest(), IIntegrationTest {}

class PersonDaoIntTest : PersonDaoTest(), IIntegrationTest {}

class OperationDaoIntTest : OperationDaoTest(), IIntegrationTest {

    @Test
    fun getByAccountOrderedByDate() {
        val budget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val accountA = Account("my own account", budget.id)
        accountDao.insert(accountA)
        val accountB = Account("my own account", budget.id)
        accountDao.insert(accountB)

        val budget2= Budget("personal budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget2)
        val masterCategory2 = MasterCategory("Fixed expense", budget2.id, null)
        masterCategoryDao.insert(masterCategory2)
        val category2 = Category("oftenAllocatedCategory", masterCategory2.id)
        categoryDao.insert(category2)
        val account2 = Account("my own account", budget2.id)
        accountDao.insert(account2)

        val operationList = listOf(
            Operation(accountA.id, TestData.nov_02_2020, category.id, 4000, 1, null, false, false),
            Operation(accountA.id, TestData.march_15_2021, category.id, 2100, 2, null, false, false),
            Operation(accountB.id, TestData.nov_03_2020, category.id, 2000, 3, "in another account", false, false),
            Operation(accountA.id, TestData.march_02_2021, category.id, 3000, 4, null, false, false),
            Operation(account2.id, TestData.nov_02_2020, category2.id, 4000, 5, "not in the right budget", false, false),
        )
        for (operation in operationList) {
            operationDao.insert(operation)
        }

        val result = operationDao.findByAccount(accountA, null)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(2100, result[0].amount)
        Assertions.assertEquals(3000, result[1].amount)
        Assertions.assertEquals(4000, result[2].amount)
    }

    @Test
    fun getByBudgetInMultipleAccount() {
        val budget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val accountA = Account("my own account", budget.id)
        accountDao.insert(accountA)
        val accountB = Account("my own account", budget.id)
        accountDao.insert(accountB)

        val budget2= Budget("personal budget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget2)
        val masterCategory2 = MasterCategory("Fixed expense", budget2.id, null)
        masterCategoryDao.insert(masterCategory2)
        val category2 = Category("oftenAllocatedCategory", masterCategory2.id)
        categoryDao.insert(category2)
        val account2 = Account("my own account", budget2.id)
        accountDao.insert(account2)

        val operationList = listOf(
            Operation(accountA.id, TestData.nov_02_2020, category.id, 4000, 1, null, false, false),
            Operation(accountB.id, TestData.nov_03_2020, category.id, 2000, 2, "in another account", false, false),
            Operation(accountA.id, TestData.march_02_2021, category.id, 3000, 3, null, false, false),
            Operation(account2.id, TestData.nov_02_2020, category2.id, 4000, 4, "not in the right budget", false, false),
        )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByBudget(budget, null)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(3000, result[0].amount)
        Assertions.assertEquals(2000, result[1].amount)
        Assertions.assertEquals(4000, result[2].amount)
    }

    @Test
    fun getByUnknownBudget() {
        val unknownBudget = Budget("professional budget", "person1", ProfileEnum.PROFILE_USER)
        val result = operationDao.findByBudget(unknownBudget, null)
        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun getByBudgetAndCategory() {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("firstCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("secondCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1000, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),
                Operation(account.id, TestData.jan_14_2022, category2.id, 321, 5),
                Operation(account.id, TestData.jan_15_2022, category.id, 546, 6)
                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByBudget(budget, category)
        Assertions.assertEquals(4, result.size)
        Assertions.assertEquals(546, result[0].amount)
        Assertions.assertEquals(3000, result[1].amount)
        Assertions.assertEquals(2000, result[2].amount)
        Assertions.assertEquals(4000, result[3].amount)
    }
    
    @Test
    fun getByBudgetAndCategoryWithNoOperation () {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("firstCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("secondCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByBudget(budget, category2)
        Assertions.assertEquals(0, result.size)
        
    }

    @Test
    fun getByBudgetAndUnknownCategory () {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("firstCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("secondCategory", masterCategory.id)
        categoryDao.insert(category2)
        val category3 = Category("thirdCategory", masterCategory.id)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1000, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByBudget(budget, category3)
        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun getByBudgetAndCategoryWithUncategorizedOperation () {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("category", masterCategory.id)
        categoryDao.insert(category)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, null, 1000, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByBudget(budget, category)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(3000, result[0].amount)
        Assertions.assertEquals(2000, result[1].amount)
        Assertions.assertEquals(4000, result[2].amount)
    }

    @Test
    fun getByAccountAndCategory() {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("firstCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("secondCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1000, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),
                Operation(account.id, TestData.jan_14_2022, category2.id, 321, 5),
                Operation(account.id, TestData.jan_15_2022, category.id, 546, 6)
                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByAccount(account, category)
        Assertions.assertEquals(4, result.size)
        Assertions.assertEquals(546, result[0].amount)
        Assertions.assertEquals(3000, result[1].amount)
        Assertions.assertEquals(2000, result[2].amount)
        Assertions.assertEquals(4000, result[3].amount)
    }
    
    @Test
    fun getByAccountAndCategoryWithNoOperation () {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("firstCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("secondCategory", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByAccount(account, category2)
        Assertions.assertEquals(0, result.size)
        
    }

    @Test
    fun getByAccountAndUnknownCategory () {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("firstCategory", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("secondCategory", masterCategory.id)
        categoryDao.insert(category2)
        val category3 = Category("thirdCategory", masterCategory.id)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1000, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByAccount(account, category3)
        Assertions.assertEquals(0, result.size)
    }

    @Test
    fun getByAccountAndCategoryWithUncategorizedOperation () {
        val budget = Budget("firstBudget", "person1", ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("category", masterCategory.id)
        categoryDao.insert(category)
        val account = Account("current account", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, category.id, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, null, 1000, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),

                )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByAccount(account, category)
        Assertions.assertEquals(3, result.size)
        Assertions.assertEquals(3000, result[0].amount)
        Assertions.assertEquals(2000, result[1].amount)
        Assertions.assertEquals(4000, result[2].amount)
    }

    @Test
    fun getOperationWithDaughterForBudget() {
        val christine = Person("Christine de Pisan", "CiteDesDames", "no@adress.col")
        personDao.insert(christine)
        val budget = Budget("DreamFactory", christine.id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Clouds", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("Cumulus", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("Cumulonimbus", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("SkyAccount", budget.id)
        accountDao.insert(account)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, null, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account.id, TestData.feb_02_2021, category2.id, 1500, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),
        )
        val operationDaughters = listOf (
            Operation(account.id, TestData.nov_02_2020, category.id, 3000, 1, null, false, false, operationList[0].id),
            Operation(account.id, TestData.nov_02_2020, category2.id, 1000, 1, null, false, false, operationList[0].id),

        )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        for (operation in operationDaughters) {
            operationDao.insert(operation)
        }
        val result = operationDao.findByBudget(budget)

        Assertions.assertEquals(4, result.size)
        Assertions.assertEquals(4000, result[3].amount)
        Assertions.assertEquals(2, result[3].daughters.size)
    }

    @Test
    fun getOperationWithDaughterInCategoryForAccount() {
        val jule = Person("Jule Gregory Charney", "MrMeteordi", "jule@eniac.com")
        personDao.insert(jule)
        val budget = Budget("DreamFactory", jule.id, ProfileEnum.PROFILE_USER)
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Clouds", budget.id, null)
        masterCategoryDao.insert(masterCategory)
        val category = Category("Cumulus", masterCategory.id)
        categoryDao.insert(category)
        val category2 = Category("Cumulonimbus", masterCategory.id)
        categoryDao.insert(category2)
        val account = Account("SkyAccount", budget.id)
        accountDao.insert(account)
        val account2 = Account("OceanAccount", budget.id)
        accountDao.insert(account2)
        val operationList = listOf(
                Operation(account.id, TestData.nov_02_2020, null, 4000, 1),
                Operation(account.id, TestData.nov_03_2020, category.id, 2000, 2),
                Operation(account2.id, TestData.feb_02_2021, category2.id, 1500, 3),
                Operation(account.id, TestData.march_02_2021, category.id, 3000, 4),
        )
        val operationDaughters = listOf (
            Operation(account.id, TestData.nov_02_2020, category.id, 3000, 1, null, false, false, operationList[0].id),
            Operation(account.id, TestData.nov_02_2020, category2.id, 1000, 1, null, false, false, operationList[0].id),

        )
        for (operation in operationList) {
            operationDao.insert(operation)
        }
        for (operation in operationDaughters) {
            operationDao.insert(operation)
        }
        var result = operationDao.findByAccount(account, category2)

        Assertions.assertEquals(1, result.size)
        Assertions.assertEquals(4000, result[0].amount)

        result = operationDao.findByBudget(budget, category2)

        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(1500, result[0].amount)
        Assertions.assertEquals(4000, result[1].amount)
    }
}
