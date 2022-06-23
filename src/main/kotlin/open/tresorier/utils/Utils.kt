package open.tresorier.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.Properties as JavaProperties
import open.tresorier.utils.PropertiesEnum.*


object Utils {

    fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

    fun getLogger(name: String): Logger = LoggerFactory.getLogger(name)

    fun newLine() : String {
        return System.lineSeparator()
    }
}
