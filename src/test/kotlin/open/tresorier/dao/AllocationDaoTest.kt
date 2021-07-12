package open.tresorier.dao


import open.tresorier.dependenciesinjection.ITest
import open.tresorier.exception.TresorierException
import open.tresorier.model.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

open class AllocationDaoTest : ITest {

    val allocationDao by inject<IAllocationDao>()
    val personDao by inject<IPersonDao>()
    val budgetDao by inject<IBudgetDao>()
    val masterCategoryDao by inject<IMasterCategoryDao>()
    val categoryDao by inject<ICategoryDao>()

    @Test
    fun getOwner() {
        val category = categoryDao.getById("category1")
        val month = Month.createFromComparable(202102)
        val allocation = allocationDao.getByIdentifiers(category, month)
        val expectedOwner = personDao.getById("person1")
        val owner = allocationDao.getOwner(allocation)
        Assertions.assertEquals(expectedOwner.email, owner.email)
    }

    @Test
    fun getOwnerForUnstored() {
        val allocation = Allocation(Month(12,2020),"251",-25425)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            allocationDao.getOwner(allocation)
        }
        Assertions.assertEquals("the given object appears to have no owner", exception.message)
    }

    @Test
    fun updateAllocation() {
        val month = Month(11,1225)
        val category = categoryDao.getById("category1")
        val allocation = Allocation(month, category.id, -25425)
        val allocationUpdated = Allocation(month, category.id, 30000)
        allocationDao.insertOrUpdate(allocation)
        allocationDao.insertOrUpdate(allocationUpdated)
        val allocationInDb = allocationDao.getByIdentifiers(category, month)
        Assertions.assertEquals(30000, allocationInDb.amount)
    }

    @Test
    fun cannotHaveAllocationForInvalidMonth() {
        val allocation = Allocation(Month(14,1225), "category1", -25425)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            allocationDao.insertOrUpdate(allocation)
        }
        Assertions.assertEquals("could not insert allocation : $allocation", exception.message)
    }

    @Test
    fun cannotHaveAllocationForInvalidMonth2() {
        val allocation = Allocation(Month(-5, 1225), "category1", -25425)
        val exception = Assertions.assertThrows(TresorierException::class.java) {
            allocationDao.insertOrUpdate(allocation)
        }
        Assertions.assertEquals("could not insert allocation : $allocation", exception.message)
    }

    @Test
    fun getAllAllocationsOfBudget() {
        val budget = Budget("wellAllocatedBudget", "person1")
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val allocationList = listOf(
            Allocation(Month(1,2020),category.id,4000),
            Allocation(Month(12,2020),category.id,2000)
        )
        for (allocation in allocationList) {
            allocationDao.insertOrUpdate(allocation)
        }
        val result = allocationDao.findByBudget(budget)
        Assertions.assertEquals(2, result.size)
        Assertions.assertEquals(allocationList[0].month.comparable, result[0].month.comparable)
        Assertions.assertEquals(allocationList[0].categoryId, result[0].categoryId)
        Assertions.assertEquals(allocationList[1].month.comparable, result[1].month.comparable)
        Assertions.assertEquals(allocationList[1].categoryId, result[1].categoryId)

    }


    @Test
    fun getAllAllocationsOfBudgetUntilMonth() {
        val budget = Budget("wellAllocatedBudget", "person1")
        budgetDao.insert(budget)
        val masterCategory = MasterCategory("Fixed expense", budget.id)
        masterCategoryDao.insert(masterCategory)
        val category = Category("oftenAllocatedCategory", masterCategory.id)
        categoryDao.insert(category)
        val allocationList = listOf(
                Allocation(Month(1,2020),category.id,4000),
                Allocation(Month(12,2020),category.id,2000),
                Allocation(Month(1,2021),category.id,2000),
                Allocation(Month(2,2022),category.id,2000),
                Allocation(Month(3,2022),category.id,2000)

        )
        for (allocation in allocationList) {
            allocationDao.insertOrUpdate(allocation)
        }
        val result = allocationDao.findByBudget(budget, Month(2,2022))
        Assertions.assertEquals(4, result.size)
        Assertions.assertEquals(allocationList[0].month.comparable, result[0].month.comparable)
        Assertions.assertEquals(allocationList[1].month.comparable, result[1].month.comparable)
        Assertions.assertEquals(allocationList[2].month.comparable, result[2].month.comparable)
        Assertions.assertEquals(allocationList[3].month.comparable, result[3].month.comparable)

        Assertions.assertEquals(allocationList[0].categoryId, result[0].categoryId)
        Assertions.assertEquals(allocationList[1].categoryId, result[1].categoryId)
        Assertions.assertEquals(allocationList[2].categoryId, result[2].categoryId)
        Assertions.assertEquals(allocationList[3].categoryId, result[3].categoryId)
    }

    @Test
    fun getAllAllocationsOfNonExistingBudget() {
        val budget = Budget("wellAllocatedBudget", "person1")
        val result = allocationDao.findByBudget(budget)
        Assertions.assertEquals(0, result.size)
    }
}
