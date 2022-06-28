package open.tresorier.model
import java.util.*

open class DbObject(p_id: String?, p_deleted: Boolean?) {
    val id: String =
        p_id ?: UUID.randomUUID().toString() // is "val" instead of "var" because it can be read but never changed
    var deleted: Boolean = p_deleted ?: false
}
