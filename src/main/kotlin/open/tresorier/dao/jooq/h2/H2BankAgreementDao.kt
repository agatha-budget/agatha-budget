package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IBankAgreementDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.tables.records.BankAgreementRecord
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.generated.jooq.test.public_.tables.daos.BankAgreementDao
import open.tresorier.generated.jooq.test.public_.Tables.PERSON
import open.tresorier.generated.jooq.test.public_.Tables.BUDGET
import open.tresorier.generated.jooq.test.public_.Tables.BANK_AGREEMENT
import org.jooq.impl.DSL
import open.tresorier.model.banking.BankAgreement
import open.tresorier.model.Person
import open.tresorier.model.Budget
import org.jooq.Configuration
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

    override fun getOwner(bankAgreement: BankAgreement) : Person {
        try {
            val owner: PersonRecord = this.query.select().from(PERSON)
                .join(BUDGET).on(BUDGET.ID.eq(bankAgreement.budgetId))
                .where(PERSON.ID.eq(BUDGET.ID))
                .fetchAny().into(PERSON)
            return H2PersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
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

    private fun toJooqBankAgreement(bankAgreement: BankAgreement): JooqBankAgreement {
        return JooqBankAgreement(
            bankAgreement.id,
            bankAgreement.budgetId,
            bankAgreement.bankId,
            bankAgreement.validUntil,
            bankAgreement.nordigenRequisitionId,
            bankAgreement.archived,
            bankAgreement.deleted
        )
    }

    private fun toBankAgreement(jooqBankAgreement: JooqBankAgreement): BankAgreement {
        return BankAgreement(
            jooqBankAgreement.budgetId,
            jooqBankAgreement.bankId,
            jooqBankAgreement.validUntil,
            jooqBankAgreement.nordigenRequisitionId,
            jooqBankAgreement.archived,
            jooqBankAgreement.id,
            jooqBankAgreement.deleted
        )
    }
}
