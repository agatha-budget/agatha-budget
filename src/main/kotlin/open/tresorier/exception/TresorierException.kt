package open.tresorier.exception

import open.tresorier.utils.Utils
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*


open class TresorierException (
    private val p_message: String,
    private val p_catchedException: Exception? = null
) : Exception(p_message) {


    private val catchedException : Exception? = p_catchedException
    val id : String = UUID.randomUUID().toString()

    init {
        this.logException()
    }

    private fun logException() {
        Utils.getLogger().error(
            "exception_code : " + this.id
                     + Utils.newLine() +
                     "exception_message : " + this.message
                     + Utils.newLine() +
                     "catched_exception_msg : " + this.catchedException?.message
                     + Utils.newLine() +
                     "catched_exception_trace : " + this.catchedException?.let {getStackTrace(it)}
        )
    }

    private fun getStackTrace(e: Exception): String {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        return sw.toString()
    }



}
