package open.tresorier.services

import open.tresorier.mailing.IMailingPort
import open.tresorier.model.Person

class MailingService(private val mailingPort: IMailingPort) {

    fun addPersonToMailingList(person: Person) {
        this.mailingPort.add(person)
    }

}