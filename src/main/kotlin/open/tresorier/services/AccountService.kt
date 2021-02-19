package open.tresorier.services

import open.tresorier.dao.IAccountDao
import open.tresorier.exception.TresorierIllegalException
import open.tresorier.model.Account
import open.tresorier.model.Person

class AccountService(private val accountDao: IAccountDao) {

    fun create(person: Person, name: String): Account {
        val account = Account(name, person.id)
        return accountDao.insert(account)
    }

    fun update(person: Person, account: Account, newName: String): Account {
        cancelIfUserIsUnauthorized(person, account)
        account.name = newName
        return accountDao.update(account)
    }

    fun getById(person: Person, id: String): Account {
        val account = accountDao.getById(id)
        cancelIfUserIsUnauthorized(person, account)
        return account
    }

    fun delete(person: Person, account: Account) : Account {
        cancelIfUserIsUnauthorized(person, account)
        account.deleted = true
        return accountDao.update(account)
    }

    private fun cancelIfUserIsUnauthorized(person: Person, account: Account) {
        val owner = accountDao.getOwner(account)
        if (owner.id != person.id) {
            throw TresorierIllegalException("user " + person.id + "isn't allowed to interact with account " + account.id)
        }
    }
}
