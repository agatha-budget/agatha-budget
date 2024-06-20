package open.tresorier.model
import java.util.*

open class DbObject(idP: String?, deletedP: Boolean?) {
    val id: String =
        idP ?: UUID.randomUUID().toString() // is "val" instead of "var" because it can be read but never changed
    var deleted: Boolean = deletedP ?: false
}
