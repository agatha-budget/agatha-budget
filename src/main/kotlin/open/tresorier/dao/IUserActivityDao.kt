package open.tresorier.dao

import open.tresorier.model.UserActivity
import open.tresorier.model.enum.ActionEnum
import open.tresorier.model.Day

interface IUserActivityDao {
    fun insert(userActivity: UserActivity): UserActivity
    fun update(userActivity: UserActivity): UserActivity
    fun getByUserId(userId: String): List<UserActivity>
    fun getByDate(date: Long): List<UserActivity>
    fun getByAction(action: ActionEnum): List<UserActivity>
}