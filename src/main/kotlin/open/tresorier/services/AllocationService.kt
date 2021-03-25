package open.tresorier.services

import open.tresorier.dao.IAllocationDao
import open.tresorier.model.Allocation
import open.tresorier.model.Person

class AllocationService(private val allocationDao: IAllocationDao, private val authorizationService: AuthorizationService) {

    fun update(person: Person, allocation: Allocation, newAmount: Double): Allocation {
        authorizationService.cancelIfUserIsUnauthorized(person, allocation)
        allocation.amount = newAmount
        return allocationDao.update(allocation)
    }
    
    fun getById(person: Person, id: String): Allocation {
        val allocation = allocationDao.getById(id)
        authorizationService.cancelIfUserIsUnauthorized(person, allocation)
        return allocation
    }
}