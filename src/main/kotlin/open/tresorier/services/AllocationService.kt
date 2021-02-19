package open.tresorier.services

import open.tresorier.dao.IAllocationDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Allocation
import open.tresorier.model.Person

class AllocationService(private val allocationDao: IAllocationDao) {

    fun update(person: Person, allocation: Allocation, newAmount: Double): Allocation {
        cancelIfUserIsUnauthorized(person, allocation)
        allocation.amount = newAmount
        return allocationDao.update(allocation)
    }
    
    fun getById(person: Person, id: String): Allocation {
        val allocation = allocationDao.getById(id)
        cancelIfUserIsUnauthorized(person, allocation)
        return allocation
    }

    private fun cancelIfUserIsUnauthorized(person: Person, allocation: Allocation) {
        val owner = allocationDao.getOwner(allocation)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with allocation " + allocation.id)
        }
    }
}