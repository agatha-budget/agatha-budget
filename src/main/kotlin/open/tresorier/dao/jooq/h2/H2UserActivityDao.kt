package open.tresorier.dao.jooq.h2

import open.tresorier.dao.IUserActivityDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.UserActivityDao
import open.tresorier.generated.jooq.test.public_.tables.records.UserActivityRecord
import open.tresorier.model.UserActivity
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.model.Day
import open.tresorier.generated.jooq.test.public_.tables.pojos.UserActivity as JooqUserActivity
import open.tresorier.model.enum.ActionEnum

class H2UserActivityDao(val configuration: Configuration): IUserActivityDao {

    private val generatedDao: UserActivityDao = UserActivityDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(userActivity: UserActivity): UserActivity {
        val jooqUserActivity = this.toJooqUserActivity(userActivity)

        try {
            this.generatedDao.insert(jooqUserActivity)
            return userActivity

        } catch (e: Exception) {
            throw TresorierException("could not create new userActivity", e)
        }
    }

    override fun update(userActivity: UserActivity): UserActivity {
        val jooqUserActivity = this.toJooqUserActivity(userActivity)
        this.generatedDao.update(jooqUserActivity)
        return userActivity
    }

    override fun getByUserId(userId: String): List<UserActivity> {
        val jooqUserActivityList = this.query
            .select()
            .from(USER_ACTIVITY)
            .where(USER_ACTIVITY.USER_ID.eq(userId))
            .fetch().into(USER_ACTIVITY)
        
        val userActivityList: MutableList<UserActivity> = mutableListOf()
        for (userActivityRecord : UserActivityRecord in jooqUserActivityList) {
            val userActivity = this.toUserActivity(userActivityRecord)
            userActivityList.add(userActivity)
        }

        return userActivityList
    }

    override fun getByDate(date: Long): List<UserActivity> {
        val jooqUserActivityList = this.query
            .select()
            .from(USER_ACTIVITY)
            .where(USER_ACTIVITY.DATE.eq(date))
            .fetch().into(USER_ACTIVITY)
        
        val userActivityList: MutableList<UserActivity> = mutableListOf()
        for (userActivityRecord : UserActivityRecord in jooqUserActivityList) {
            val userActivity = this.toUserActivity(userActivityRecord)
            userActivityList.add(userActivity)
        }

        return userActivityList
    }

    override fun getByAction(action: ActionEnum): List<UserActivity> {
        val jooqUserActivityList = this.query
            .select()
            .from(USER_ACTIVITY)
            .where(USER_ACTIVITY.ACTION.eq(action.toString()))
            .fetch().into(USER_ACTIVITY)
        
        val userActivityList: MutableList<UserActivity> = mutableListOf()
        for (userActivityRecord : UserActivityRecord in jooqUserActivityList) {
            val userActivity = this.toUserActivity(userActivityRecord)
            userActivityList.add(userActivity)
        }

        return userActivityList
    }

    private fun toJooqUserActivity(userActivity: UserActivity): JooqUserActivity {
        return JooqUserActivity(
            userActivity.id,
            userActivity.userId,
            userActivity.date,
            userActivity.action.toString()
        )
    }

    fun toUserActivity(jooqUserActivity: JooqUserActivity?): UserActivity? {
        return if (jooqUserActivity == null)
            null
        else UserActivity(
            jooqUserActivity.userId,
            jooqUserActivity.date,
            ActionEnum.valueOf(jooqUserActivity.action),
            jooqUserActivity.id
        )
    }
    fun toUserActivity(recordUserActivity: UserActivityRecord): UserActivity {
        return UserActivity(
            recordUserActivity.userId,
            recordUserActivity.date,
            ActionEnum.valueOf(recordUserActivity.action),
            recordUserActivity.id
        )
    }
}