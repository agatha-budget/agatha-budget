package open.tresorier.services

import open.tresorier.dao.IAllocationDao
import open.tresorier.model.Allocation
import open.tresorier.model.Category
import open.tresorier.model.Month
import open.tresorier.model.Person

class AllocationService(private val allocationDao: IAllocationDao, private val authorizationService: AuthorizationService) {

    fun insertOrUpdate(person: Person, month: Month, category: Category, amount: Int): Allocation {
        authorizationService.cancelIfUserIsUnauthorized(person, category)
        val allocation = Allocation(month, category.id, amount)
        return allocationDao.insertOrUpdate(allocation)
    }
    
    fun getByIdentifier(person: Person, category: Category, month: Month): Allocation {
        val allocation = allocationDao.getByIdentifiers(category, month)
        authorizationService.cancelIfUserIsUnauthorized(person, allocation)
        return allocation
    }
}