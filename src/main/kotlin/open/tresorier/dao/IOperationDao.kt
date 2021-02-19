package open.tresorier.dao

import open.tresorier.model.Operation
import open.tresorier.model.Person

interface IOperationDao {
    fun insert(operation: Operation) : Operation
    fun update(operation: Operation) : Operation
    fun delete(operation: Operation)
    fun getById(id: String): Operation
    fun getOwner(operation: Operation) : Person
}