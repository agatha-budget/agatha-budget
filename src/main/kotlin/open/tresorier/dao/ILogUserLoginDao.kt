package open.tresorier.dao

import open.tresorier.model.LogUserLogin
import open.tresorier.model.enum.ActionEnum
import open.tresorier.model.Day

interface ILogUserLoginDao {
    fun insert(logUserLogin: LogUserLogin): LogUserLogin
    fun update(logUserLogin: LogUserLogin): LogUserLogin
    fun getByUserId(userId: String): List<LogUserLogin>
    fun getByDate(day: Day): List<LogUserLogin>
    fun getByAction(action: ActionEnum): List<LogUserLogin>
}