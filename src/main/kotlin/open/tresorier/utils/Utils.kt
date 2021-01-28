package open.tresorier.utils

import java.util.Properties
import java.io.FileInputStream
import org.slf4j.LoggerFactory
import org.slf4j.Logger

object Utils {

    fun getPropertiesFromFile(fileRelativePath : String) : Properties {
        val properties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "/" + fileRelativePath;
        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)
        return properties
    }

    fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

    fun getLogger(name: String): Logger = LoggerFactory.getLogger(name)

    fun newLine() : String {
        return System.lineSeparator()
    }
}
