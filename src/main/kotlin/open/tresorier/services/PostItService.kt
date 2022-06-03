package open.tresorier.services

import open.tresorier.dao.IPostItDao
import open.tresorier.model.*

class PostItService(private val postItDao: IPostItDao, private val authorizationService: AuthorizationService) {

    fun update(person: Person, postIt: PostIt, budget: Budget, newText: String): PostIt {
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        newText?.let {postIt.text = it}
        return postItDao.update(postIt)
    }
    fun getByIdentifiers(person: Person, budget: Budget, month: Month): PostIt {
        val postIt = postItDao.getByIdentifiers(budget, month)
        authorizationService.cancelIfUserIsUnauthorized(person, budget)
        return postIt        
    }
}