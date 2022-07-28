package open.tresorier.services

import open.tresorier.dao.IPostItDao
import open.tresorier.model.*

class PostItService(private val postItDao: IPostItDao, private val authorizationService: AuthorizationService) {

    fun insertOrUpdate(person: Person, month: Month, budget: Budget, text: String): PostIt {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val newPostIt = PostIt(month, budget.id, text)
        return postItDao.insertOrUpdate(newPostIt)
    }
    fun getByIdentifiers(person: Person, budget: Budget, month: Month): PostIt? {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        val postIt = postItDao.getByIdentifiers(budget, month)
        return postIt        
    }
}