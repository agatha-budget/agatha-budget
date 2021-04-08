package open.tresorier.dao.jooq.test

import open.tresorier.dao.IBudgetDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables
import open.tresorier.generated.jooq.test.public_.tables.records.PersonRecord
import open.tresorier.generated.jooq.test.public_.tables.daos.BudgetDao
import open.tresorier.model.Budget
import open.tresorier.model.Person
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.test.public_.tables.pojos.Budget as JooqBudget

class JooqTestBudgetDao (val configuration : Configuration) : IBudgetDao {

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
        try {
            val owner: PersonRecord = this.query.select().from(Tables.PERSON)
                    .join(Tables.BUDGET).on(Tables.BUDGET.PERSON_ID.eq(Tables.PERSON.ID))
                    .where(Tables.BUDGET.ID.eq(budget.id))
                    .fetchAny().into(Tables.PERSON)
            return JooqTestPersonDao.toPerson(owner)
        } catch (e : Exception) {
            throw TresorierException("the given object appears to have no owner")
        }
    }

    private fun toJooqBudget(budget : Budget) : JooqBudget {
        return JooqBudget(budget.id,
                          budget.personId,
                          budget.name,
                          budget.deleted
        )
    }

    private fun toBudget(jooqBudget : JooqBudget?)  : Budget? {
        return if (jooqBudget == null)
        null
        else Budget(jooqBudget.name,
                      jooqBudget.personId,
                      jooqBudget.id,
                      jooqBudget.deleted
        )
    }
}
