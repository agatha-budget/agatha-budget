package open.tresorier.exception

import open.tresorier.utils.Utils
import open.tresorier.utils.Properties
import java.util.UUID;

class TresorierException (
    val p_catchedException: Exception,
    val p_message: String
) : Exception(p_message) {


    val catchedException : Exception = p_catchedException
    val id : String

    init {
        id = UUID.randomUUID().toString()
        this.logException()
    }

    fun logException() {
        val logger = Utils.getLogger("exception")
        logger.error("exception_code : " + this.id
                     + Utils.newLine() +
                     "exception_message : " + this.message
                     + Utils.newLine() +
                     //"catched_exception_msg : " + this.catchedException.getMessage()
                     //+ Utils.newLine() +
                     "catched_exception_trace : " + this.catchedException.getStackTrace()
        )
    }
}
