package open.tresorier.dao.jooq

import org.jooq.exception.DataAccessException
import open.tresorier.dao.IBudgetDao
import open.tresorier.model.Budget
import open.tresorier.generated.jooq.tables.daos.BudgetDao
import open.tresorier.generated.jooq.tables.pojos.Budget as JooqBudget;

class JooqBudgetDao (val generatedDao : BudgetDao) : IBudgetDao {

    override fun insert(budget : Budget) : Budget? {
        val jooqBudget = this.toJooqBudget(budget)
        try {
        this.generatedDao.insert(jooqBudget)
        } catch (e : DataAccessException) {
            return null
        }
        return budget
    }

    override fun update(budget : Budget) : Budget?{
        val jooqBudget = this.toJooqBudget(budget)
        try {
            this.generatedDao.update(jooqBudget)
        } catch (e : DataAccessException) {
            return null
        }
        return budget
    }

    override fun delete(budget: Budget){
        val jooqBudget = this.toJooqBudget(budget)
        this.generatedDao.delete(jooqBudget)
    }

    override fun getById(id: String) : Budget? {
        val jooqBudget = this.generatedDao.fetchOneById(id)
        return this.toBudget(jooqBudget)
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
