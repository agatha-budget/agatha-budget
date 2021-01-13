package open.tresorier.dao

import open.tresorier.model.Person

interface IPersonDao {

    // return null if an error occured (duplicate unique property etc..)
    fun insert(person: Person) : Person?
    fun update(person: Person)
    fun delete(person: Person)
    fun getById(id: String): Person?
    fun getByEmail(email: String) : Person?

}
