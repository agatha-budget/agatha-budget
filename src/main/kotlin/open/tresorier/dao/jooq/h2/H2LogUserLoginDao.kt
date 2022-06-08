package open.tresorier.dao.jooq.h2

import open.tresorier.dao.ILogUserLoginDao
import open.tresorier.exception.TresorierException
import open.tresorier.generated.jooq.test.public_.Tables.*
import open.tresorier.generated.jooq.test.public_.tables.daos.LogUserLoginDao
import open.tresorier.generated.jooq.test.public_.tables.records.LogUserLoginRecord
import open.tresorier.model.LogUserLogin
import org.jooq.Configuration
import org.jooq.impl.DSL
import open.tresorier.model.Day
import open.tresorier.generated.jooq.test.public_.tables.pojos.LogUserLogin as JooqLogUserLogin
import open.tresorier.model.enum.ActionEnum

class H2LogUserLogin(val configuration: Configuration): ILogUserLoginDao {

    private val generatedDao: LogUserLoginDao = LogUserLoginDao(configuration)
    private val query = DSL.using(configuration)

    override fun insert(logUserLogin: LogUserLogin): LogUserLogin {
        val jooqLogUserLogin = this.toJooqLogUserLogin(logUserLogin)

        try {
            this.generatedDao.insert(jooqLogUserLogin)
            return logUserLogin

        } catch (e: Exception) {
            throw TresorierException("could not create new logUserLogin", e)
        }
    }

    override fun update(logUserLogin: LogUserLogin): LogUserLogin {
        val jooqLogUserLogin = this.toJooqLogUserLogin(logUserLogin)
        this.generatedDao.update(jooqLogUserLogin)
        return logUserLogin
    }

    override fun getByUserId(userId: String): List<LogUserLogin> {
        val jooqLogUserLoginList = this.query
            .select()
            .from(LOG_USER_LOGIN)
            .where(LOG_USER_LOGIN.ID_USER.eq(userId))
            .fetch().into(LOG_USER_LOGIN)
        
        val logUserLoginList: MutableList<LogUserLogin> = mutableListOf()
        for (logUserLoginRecord : LogUserLoginRecord in jooqLogUserLoginList) {
            val logUserLogin = this.toLogUserLogin(logUserLoginRecord)
            logUserLoginList.add(logUserLogin)
        }

        return logUserLoginList
    }

    override fun getByDate(day: Day): List<LogUserLogin> {
        val jooqLogUserLoginList = this.query
            .select()
            .from(LOG_USER_LOGIN)
            .where(LOG_USER_LOGIN.DATE.eq(day.comparable.toLong()))
            .fetch().into(LOG_USER_LOGIN)
        
        val logUserLoginList: MutableList<LogUserLogin> = mutableListOf()
        for (logUserLoginRecord : LogUserLoginRecord in jooqLogUserLoginList) {
            val logUserLogin = this.toLogUserLogin(logUserLoginRecord)
            logUserLoginList.add(logUserLogin)
        }

        return logUserLoginList
    }

    override fun getByAction(action: ActionEnum): List<LogUserLogin> {
        val jooqLogUserLoginList = this.query
            .select()
            .from(LOG_USER_LOGIN)
            .where(LOG_USER_LOGIN.ACTION.eq(action.toString()))
            .fetch().into(LOG_USER_LOGIN)
        
        val logUserLoginList: MutableList<LogUserLogin> = mutableListOf()
        for (logUserLoginRecord : LogUserLoginRecord in jooqLogUserLoginList) {
            val logUserLogin = this.toLogUserLogin(logUserLoginRecord)
            logUserLoginList.add(logUserLogin)
        }

        return logUserLoginList
    }

    private fun toJooqLogUserLogin(logUserLogin: LogUserLogin): JooqLogUserLogin {
        return JooqLogUserLogin(
            logUserLogin.id, // à revoir
            logUserLogin.userId, // à revoir
            logUserLogin.date,
            logUserLogin.action.toString()
        )
    }

    fun toLogUserLogin(jooqLogUserLogin: JooqLogUserLogin?): LogUserLogin? {
        return if (jooqLogUserLogin == null)
            null
        else LogUserLogin(
            jooqLogUserLogin.idUser,
            jooqLogUserLogin.date,
            ActionEnum.valueOf(jooqLogUserLogin.action),
            jooqLogUserLogin.id
        )
    }
    fun toLogUserLogin(recordLogUserLogin: LogUserLoginRecord): LogUserLogin {
        return LogUserLogin(
            recordLogUserLogin.idUser,
            recordLogUserLogin.date,
            ActionEnum.valueOf(recordLogUserLogin.action),
            recordLogUserLogin.id
        )
    }
}