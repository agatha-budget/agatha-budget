package open.tresorier.services

import open.tresorier.dao.IAllocationDao
import open.tresorier.model.Allocation
import open.tresorier.model.Category
import open.tresorier.model.Month
import open.tresorier.model.Person

class MailingService(private val mailingPort: IMailingPort) {

    fun addPersonToMailingList(person: Person): Person {
        return this.mailingPort.add(person)
    }

}