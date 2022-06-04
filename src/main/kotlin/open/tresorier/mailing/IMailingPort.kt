package open.tresorier.mailing

import open.tresorier.model.Person

interface IMailingPort {
    fun create(person: Person) : Person
}