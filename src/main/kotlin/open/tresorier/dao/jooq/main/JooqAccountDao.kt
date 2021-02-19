package open.tresorier.dao.jooq.main

import open.tresorier.dao.IAccountDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.tables.daos.AccountDao
import open.tresorier.model.Account
import org.jooq.Configuration
import open.tresorier.generated.jooq.main.tables.pojos.Account as JooqAccount

class JooqAccountDao(val configuration: Configuration) : IAccountDao {

    private val generatedDao: AccountDao = AccountDao(configuration)

    override fun insert(account: Account): Account {
        val jooqAccount = this.toJooqAccount(account)
        try {
            this.generatedDao.insert(jooqAccount)
        } catch (e: Exception) {
            throw TresorierException("could not insert account : $account", e)
        }
        return account
    }

    override fun update(account: Account): Account {
        val jooqAccount = this.toJooqAccount(account)
        try {
            this.generatedDao.update(jooqAccount)
        } catch (e: Exception) {
            throw TresorierException("could not update account : $account", e)
        }
        return account
    }

    override fun getById(id: String): Account {
        val jooqAccount = this.generatedDao.fetchOneById(id)
        return this.toAccount(jooqAccount) ?: throw TresorierException("no account found for the following id : $id")
    }

    override fun findByBudgetId(budgetId: String): List<Account> {
        val jooqAccountList = this.generatedDao.fetchByBudgetId(budgetId)
        val accountList: MutableList<Account> = mutableListOf()
        for (jooqAccount in jooqAccountList) {
            var account = this.toAccount(jooqAccount)
            account?.let { accountList.add(account) }
        }
        return accountList
    }

    private fun toJooqAccount(account: Account): JooqAccount {
        return JooqAccount(
            account.id,
            account.budgetId,
            account.name,
            account.archived,
            account.deleted
        )
    }

    private fun toAccount(jooqAccount: JooqAccount?): Account? {
        return if (jooqAccount == null)
            null
        else Account(
            jooqAccount.name,
            jooqAccount.budgetId,
            jooqAccount.archived,
            jooqAccount.id,
            jooqAccount.deleted
        )
    }
}
