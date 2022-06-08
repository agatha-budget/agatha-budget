package open.tresorier.model

import open.tresorier.model.enum.ActionEnum

class LogUserLogin (
    val userId: String? = null,
    val date: Long = 0,
    val action: ActionEnum? = ActionEnum.ACTION_LOGIN,
    id: String? = null,
    deleted: Boolean? = null
) : DbObject(id, deleted)