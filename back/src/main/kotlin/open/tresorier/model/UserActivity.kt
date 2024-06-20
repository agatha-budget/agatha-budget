package open.tresorier.model

import open.tresorier.model.enum.ActionEnum
import open.tresorier.utils.Time

class UserActivity (
    val userId: String? = null,
    val date: Long = Time.now(),
    var action: ActionEnum? = ActionEnum.ACTION_LOGIN,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)