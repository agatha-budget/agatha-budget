package open.tresorier.model
import java.util.UUID;

open class DbObject(p_id: String?, p_deleted: Boolean?) {
    val id: String // is "var" instead of "val" because it can be read but never changed
    var deleted: Boolean

    init {
        // 36 character uuid
        id = p_id ?: UUID.randomUUID().toString()
        deleted = p_deleted ?: false
    }
}
