package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IBankAgreementDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.BankAgreementDao
import open.tresorier.generated.jooq.test.public_.tables.records.BankAgreementRecord
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.model.Account
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.model.banking.BankAgreement
import open.tresorier.utils.Time
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.test.public_.tables.pojos.BankAgreement as JooqBankAgreement

class H2BankAgreementDao(val configuration: Configuration) : IBankAgreementDao {

    private val generatedDao: BankAgreementDao = BankAgreementDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(agreement: BankAgreement) : BankAgreement {
        val jooqBankAgreement = this.toJooqBankAgreement(agreement)
        try {
        this.generatedDao.insert(jooqBankAgreement)
        } catch (e : Exception) {
            throw TresorierException("could not insert bankAgreement : $agreement", e)
        }
        return agreement
    }

    override fun update(agreement: BankAgreement) : BankAgreement {
        val jooqBankAgreement = this.toJooqBankAgreement(agreement)
        try {
        this.generatedDao.update(jooqBankAgreement)
        } catch (e : Exception) {
            throw TresorierException("could not insert bankAgreement : $agreement", e)
        }
        return agreement
    }

    override fun getById(id: String): BankAgreement {
        val jooqBankAgreement = this.generatedDao.fetchOneById(id)
        return this.toBankAgreement(jooqBankAgreement) ?: throw TresorierException("no bankAgreement found for the following id : $id")
    }

    override fun getOwner(agreement: BankAgreement) : Person {
        val ownerRecord: PersonRecord? = this.query.select().from(PERSON)
            .join(BUDGET).on(BUDGET.ID.eq(agreement.budgetId))
            .where(PERSON.ID.eq(BUDGET.PERSON_ID))
            .fetchAny()?.into(PERSON)
        if (ownerRecord == null) {
            throw TresorierException("the given bankAgreement (${agreement}) appears to have no owner")
        } else {
            return H2PersonDao.toPerson(ownerRecord)
        }
    }

    override fun findByBudget(budget: Budget): List<BankAgreement> {
        val jooqBankAgreementList = this.generatedDao.fetchByBudgetId(budget.id)
        val bankAgreementList : MutableList<BankAgreement> = mutableListOf()
        for (jooqBankAgreement in jooqBankAgreementList){
            var bankAgreement = this.toBankAgreement(jooqBankAgreement)
            bankAgreement?.let{bankAgreementList.add(bankAgreement)}
        }
        return bankAgreementList
    }

    override fun findByAccount(account: Account) : BankAgreement? {
        try {
            val recordBankAgreement: BankAgreementRecord? = this.query.select().from(BANK_AGREEMENT)
                .join(BANK_ACCOUNT).on(BANK_ACCOUNT.ID.eq(account.bankAccountId))
                .where(BANK_ACCOUNT.AGREEMENT_ID.eq(BANK_AGREEMENT.ID))
                .and(BANK_AGREEMENT.TIMESTAMP.greaterThan(Time.threeMonthAgo()))
                .fetchAny()?.into(BANK_AGREEMENT)
            return toBankAgreement(recordBankAgreement)
        } catch (e : Exception) {
            return null
        } 
    }

    private fun toJooqBankAgreement(bankAgreement: BankAgreement): JooqBankAgreement {
        return JooqBankAgreement(
            bankAgreement.id,
            bankAgreement.budgetId,
            bankAgreement.bankId,
            bankAgreement.timestamp,
            bankAgreement.nordigenRequisitionId,
            bankAgreement.archived,
            bankAgreement.deleted
        )
    }

    private fun toBankAgreement(jooqBankAgreement: JooqBankAgreement?): BankAgreement? {
        if (jooqBankAgreement == null){
            return null
        }
        return BankAgreement(
            jooqBankAgreement.budgetId,
            jooqBankAgreement.bankId,
            jooqBankAgreement.timestamp,
            jooqBankAgreement.nordigenRequisitionId,
            jooqBankAgreement.archived,
            jooqBankAgreement.id,
            jooqBankAgreement.deleted
        )
    }

    private fun toBankAgreement(recordBankAgreement: BankAgreementRecord?): BankAgreement? {
        if (recordBankAgreement == null){
            return null
        }
        return BankAgreement(
            recordBankAgreement.budgetId,
            recordBankAgreement.bankId,
            recordBankAgreement.timestamp,
            recordBankAgreement.nordigenRequisitionId,
            recordBankAgreement.archived,
            recordBankAgreement.id,
            recordBankAgreement.deleted
        )
    }
}
