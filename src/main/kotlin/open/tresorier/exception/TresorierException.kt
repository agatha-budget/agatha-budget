package open.tresorier.exception

import open.tresorier.utils.Utils
import java.util.*

class TresorierException (
    val p_message: String,
    val p_catchedException: Exception? = null
) : Exception(p_message) {


    val catchedException : Exception? = p_catchedException
    val id : String

    init {
        id = UUID.randomUUID().toString()
        this.logException()
    }

    fun logException() {
        Utils.getLogger().error(
            "exception_code : " + this.id
                     + Utils.newLine() +
                     "exception_message : " + this.message
                     + Utils.newLine() +
                     "catched_exception_msg : " + this.catchedException?.message
                     + Utils.newLine() +
                     "catched_exception_trace : " + this.catchedException?.stackTrace
        )
    }
}
