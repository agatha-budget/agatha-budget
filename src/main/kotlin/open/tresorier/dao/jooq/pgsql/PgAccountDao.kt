package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IAccountDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.BUDGET
import open.tresorier.generated.jooq.main.Tables.PERSON
import open.tresorier.generated.jooq.main.tables.daos.AccountDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.Account
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.main.tables.pojos.Account as JooqAccount


class PgAccountDao(val configuration: Configuration) : IAccountDao {

    private val generatedDao: AccountDao = AccountDao(configuration)
    private val query = DSL.using(configuration)

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
            val account = this.toAccount(jooqAccount)
            account?.let { accountList.add(account) }
        }
        return accountList
    }

    override fun getOwner(account: Account): Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(BUDGET).on(BUDGET.ID.eq(account.budgetId))
                .where(PERSON.ID.eq(BUDGET.PERSON_ID))
                .fetchAny().into(PERSON)
            return PgPersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
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