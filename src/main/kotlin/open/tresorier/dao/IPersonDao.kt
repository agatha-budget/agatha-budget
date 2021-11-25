package open.tresorier.dao

import open.tresorier.model.Person

interface IPersonDao {
    fun insert(person: Person) : Person
    fun update(person: Person)
    fun getById(id: String): Person
    fun getByEmail(email: String) : Person
    fun getByBillingId(billingId: String): Person
}
