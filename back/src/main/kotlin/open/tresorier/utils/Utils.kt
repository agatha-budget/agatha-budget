package open.tresorier.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object Utils {

    fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

    fun getLogger(name: String): Logger = LoggerFactory.getLogger(name)

    fun newLine() : String {
        return System.lineSeparator()
    }

    fun truncStringToMax(string: String, max: Int) : String {
        if (max < 0) {
            return ""
        }
        if (string.length <= max) {
            return string;
        } else {
            return string.substring(0, max);
        }
    }
}
