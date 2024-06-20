package open.tresorier.dao.jooq.pgsql

import open.tresorier.dao.IBudgetDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.main.Tables.BUDGET
import open.tresorier.generated.jooq.main.Tables.PERSON
import open.tresorier.generated.jooq.main.tables.daos.BudgetDao
import open.tresorier.generated.jooq.main.tables.records.PersonRecord
import open.tresorier.model.Budget
import open.tresorier.model.Person
import open.tresorier.model.enum.ProfileEnum
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.main.tables.pojos.Budget as JooqBudget

class PgBudgetDao (val configuration : Configuration) : IBudgetDao {

    private val generatedDao : BudgetDao = BudgetDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(budget : Budget) : Budget {
        val jooqBudget = this.toJooqBudget(budget)
        try {
        this.generatedDao.insert(jooqBudget)
        } catch (e : Exception) {
            throw TresorierException("could not insert budget : $budget", e)
        }
        return budget
    }

    override fun update(budget : Budget) : Budget {
        val jooqBudget = this.toJooqBudget(budget)
        try {
            this.generatedDao.update(jooqBudget)
        } catch (e : Exception) {
            throw TresorierException("could not update budget : $budget", e)
        }
        return budget
    }

    override fun getById(id: String) : Budget {
        val jooqBudget = this.generatedDao.fetchOneById(id)
        return this.toBudget(jooqBudget) ?: throw TresorierException("no budget found for the following id : $id")
    }

    override fun findByPersonId(personId: String) : List<Budget> {
        val jooqBudgetList = this.generatedDao.fetchByPersonId(personId)
        val budgetList : MutableList<Budget> = mutableListOf()
        for (jooqBudget in jooqBudgetList){
            var budget = this.toBudget(jooqBudget)
            budget?.let{budgetList.add(budget)}
        }
        return budgetList
    }

    override fun getOwner(budget: Budget): Person {
        val ownerRecord: PersonRecord? = this.query.select().from(PERSON)
            .join(BUDGET).on(BUDGET.PERSON_ID.eq(PERSON.ID))
            .where(BUDGET.ID.eq(budget.id))
            .fetchAny()?.into(PERSON)
        if (ownerRecord == null) {
            throw TresorierException("the given budget (${budget}) appears to have no owner")
        } else {
            return PgPersonDao.toPerson(ownerRecord)
        }
    }


    private fun toJooqBudget(budget : Budget) : JooqBudget {
        return JooqBudget(budget.id,
                          budget.personId,
                          budget.name,
                          budget.deleted,
                          budget.profile.toString()
        )
    }

    private fun toBudget(jooqBudget : JooqBudget?)  : Budget? {
        return if (jooqBudget == null)
        null
        else Budget(jooqBudget.name,
                      jooqBudget.personId,
                      ProfileEnum.valueOf(jooqBudget.profile),
                      jooqBudget.id,
                      jooqBudget.deleted
        )
    }
}
