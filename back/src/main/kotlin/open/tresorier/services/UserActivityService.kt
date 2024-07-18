package open.tresorier.services

import open.tresorier.dao.IUserActivityDao
import open.tresorier.model.Person
import open.tresorier.model.UserActivity
import open.tresorier.model.enum.ActionEnum


class UserActivityService(private val userActivityDao: IUserActivityDao) {

    fun create(user: Person, date: Long, action: ActionEnum): UserActivity {
        val userActivity = UserActivity(user.id, date, action)
        userActivityDao.insert(userActivity)
        return userActivity
    }

    fun update(userActivity: UserActivity, newAction: ActionEnum): UserActivity {
        userActivity.action = newAction
        return userActivityDao.update(userActivity)
    }

    fun getByUser(user: Person): List<UserActivity> {
        return userActivityDao.getByUserId(user.id)
    }

    fun getByDate(date: Long): List<UserActivity> {
        return userActivityDao.getByDate(date)
    }

    fun getByAction(action: ActionEnum): List<UserActivity> {
        return userActivityDao.getByAction(action)
    }
}
