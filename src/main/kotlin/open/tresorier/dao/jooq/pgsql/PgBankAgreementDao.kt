package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IBankAgreementDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.tables.records.BankAgreementRecord
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.generated.jooq.main.tables.daos.BankAgreementDao
import open.tresorier.generated.jooq.main.Tables.PERSON
import open.tresorier.generated.jooq.main.Tables.BANK_AGREEMENT
import org.jooq.impl.DSL
import open.tresorier.model.banking.BankAgreement
import open.tresorier.model.Person
import org.jooq.Configuration
import open.tresorier.generated.jooq.main.tables.pojos.BankAgreement as JooqBankAgreement

class PgBankAgreementDao(val configuration: Configuration) : IBankAgreementDao {

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
                    .join(BANK_AGREEMENT).on(BANK_AGREEMENT.PERSON_ID.eq(PERSON.ID))
                    .where(BANK_AGREEMENT.ID.eq(bankAgreement.id))
                    .fetchAny().into(PERSON)
            return PgPersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    override fun findByPerson(person: Person): List<BankAgreement> {
        val jooqBankAgreementList = this.generatedDao.fetchByPersonId(person.id)
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
            bankAgreement.personId,
            bankAgreement.bankId,
            bankAgreement.validUntil,
            bankAgreement.nordigenRequisitionId,
            bankAgreement.archived,
            bankAgreement.deleted
        )
    }

    private fun toBankAgreement(jooqBankAgreement: JooqBankAgreement): BankAgreement {
        return BankAgreement(
            jooqBankAgreement.personId,
            jooqBankAgreement.bankId,
            jooqBankAgreement.validUntil,
            jooqBankAgreement.nordigenRequisitionId,
            jooqBankAgreement.archived,
            jooqBankAgreement.id,
            jooqBankAgreement.deleted
        )
    }
}
