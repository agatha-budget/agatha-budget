package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IPostItDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.PostItDao
import open.tresorier.generated.jooq.test.public_.tables.records.PostItRecord
import open.tresorier.model.*
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.generated.jooq.test.public_.tables.pojos.PostIt as JooqPostIt

class H2PostItDao(val configuration: Configuration) : IPostItDao {

    private val generatedDao: PostItDao = PostItDao(configuration)
    private val query = DSL.using(configuration)

    override fun insertOrUpdate(postIt: PostIt): PostIt {
        val jooqPostIt = this.toJooqPostIt(postIt)
        try {
            val existingPostIt : PostItRecord? = this.getRecordByIdentifiers(postIt.budgetId, postIt.month)
            existingPostIt?.let { this.generatedDao.update(jooqPostIt) } ?: this.generatedDao.insert(jooqPostIt)
        } catch(e: Exception) {
            throw TresorierException("could not insert post-it : $postIt", e)
        }
        return postIt
    }

    override fun getByIdentifiers(budget: Budget, month: Month): PostIt {
        val postItRecord : PostItRecord ? = getRecordByIdentifiers(budget.id, month)
        return postItRecord?.let {this.toPostIt(it)}
            ?: PostIt(month, budget.id, "")
    }

    private fun getRecordByIdentifiers(budgetId: String, month: Month): PostItRecord ? {
        return this.query
            .select()
            .from(POST_IT)
            .where(POST_IT.BUDGET_ID.eq(budgetId))
            .and(POST_IT.MONTH.eq(month.comparable))
            .fetchAny()?.into(POST_IT)
    }


    private fun toJooqPostIt(postIt: PostIt): JooqPostIt {
        return JooqPostIt(
            postIt.budgetId,
            postIt.month.comparable,
            postIt.text
        )
    }

    private fun toPostIt(postItRecord: PostItRecord): PostIt {
        return PostIt(
            Month.createFromComparable(postItRecord.month),
            postItRecord.budgetId,
            postItRecord.text,
        )
    }
}