package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IBankAccountDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.tables.records.BankAccountRecord
import open.tresorier.generated.jooq.main.tables.daos.BankAccountDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.generated.jooq.main.Tables.*
import org.jooq.impl.DSL
import open.tresorier.model.banking.BankAccount
import open.tresorier.model.banking.BankAgreement
import open.tresorier.model.Person
import org.jooq.Configuration
import open.tresorier.generated.jooq.main.tables.pojos.BankAccount as JooqBankAccount

class PgBankAccountDao(val configuration: Configuration) : IBankAccountDao {

    private val generatedDao: BankAccountDao = BankAccountDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(bankAccount: BankAccount) : BankAccount {
        val jooqBankAccount = this.toJooqBankAccount(bankAccount)
        try {
        this.generatedDao.insert(jooqBankAccount)
        } catch (e : Exception) {
            throw TresorierException("could not insert BankAccount : $bankAccount", e)
        }
        return bankAccount
    }

    override fun update(bankAccount: BankAccount) : BankAccount {
        val jooqBankAccount = this.toJooqBankAccount(bankAccount)
        try {
        this.generatedDao.update(jooqBankAccount)
        } catch (e : Exception) {
            throw TresorierException("could not insert BankAccount : $bankAccount", e)
        }
        return bankAccount
    }

    override fun getById(id: String): BankAccount {
        val jooqBankAccount = this.generatedDao.fetchOneById(id)
        return this.toBankAccount(jooqBankAccount) ?: throw TresorierException("no BankAccount found for the following id : $id")
    }

    override fun getOwner(bankAccount: BankAccount) : Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                    .join(BUDGET).on(BUDGET.PERSON_ID.eq(PERSON.ID))
                    .join(BANK_AGREEMENT).on(BANK_AGREEMENT.BUDGET_ID.eq(BUDGET.ID))
                    .join(BANK_ACCOUNT).on(BANK_ACCOUNT.AGREEMENT_ID.eq(BANK_AGREEMENT.ID))
                    .where(BANK_ACCOUNT.ID.eq(bankAccount.id))
                    .fetchAny().into(PERSON)
            return PgPersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    override fun findByAgreement(agreement: BankAgreement): List<BankAccount> {
        val jooqBankAccountList = this.generatedDao.fetchByAgreementId(agreement.id)
        val BankAccountList : MutableList<BankAccount> = mutableListOf()
        for (jooqBankAccount in jooqBankAccountList){
            var BankAccount = this.toBankAccount(jooqBankAccount)
            BankAccount?.let{BankAccountList.add(BankAccount)}
        }
        return BankAccountList
    }

    private fun toJooqBankAccount(BankAccount: BankAccount): JooqBankAccount {
        return JooqBankAccount(
            BankAccount.id,
            BankAccount.name,
            BankAccount.agreementId,
            BankAccount.deleted
        )
    }

    private fun toBankAccount(jooqBankAccount: JooqBankAccount): BankAccount {
        return BankAccount(
            jooqBankAccount.name,
            jooqBankAccount.agreementId,
            jooqBankAccount.id,
            jooqBankAccount.deleted
        )
    }
}
