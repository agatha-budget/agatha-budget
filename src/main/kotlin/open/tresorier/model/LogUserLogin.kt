package open.tresorier.model

import open.tresorier.model.enum.ActionEnum

class LogUserLogin (
    id: String? = null,
    id_user: String? = null,
    date: Long = 0,
    action: ActionEnum? = 'ACTION_LOGIN'
)