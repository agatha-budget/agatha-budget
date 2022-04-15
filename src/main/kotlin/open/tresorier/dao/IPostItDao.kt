package open.tresorier.dao

import open.tresorier.model.*

interface IPostItDao {
    fun update(postIt: PostIt) : PostIt
    fun getByIdentifiers(budget: Budget, month: Month): PostIt
}