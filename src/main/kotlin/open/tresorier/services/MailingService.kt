package open.tresorier.services

import open.tresorier.mailing.IMailingPort
import open.tresorier.model.Person

class MailingService(private val mailingPort: IMailingPort) {

    fun addPersonToMailingList(person: Person) {
        try {
            this.mailingPort.add(person)
        } catch (e: Exception) {
            // don't do anything, the error will be logged
            // but the user can continue without interruption
        }
    }

}