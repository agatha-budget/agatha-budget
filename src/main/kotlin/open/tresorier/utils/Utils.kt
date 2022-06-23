package open.tresorier.utils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.io.File
import kotlin.text.Regex
import java.util.Properties as JavaProperties
import open.tresorier.utils.PropertiesEnum.*


object Utils {

    fun getPropertiesFromFile(fileRelativePath : String) : JavaProperties {
        val properties = JavaProperties()
        val path = System.getProperty("user.dir") + "/" + fileRelativePath;
        val inputStream = FileInputStream(path)
        properties.load(inputStream)
        return properties
    }

    fun setPropertyInFile(fileRelativePath : String, key : String, value : String) {
        val path = System.getProperty("user.dir") + "/" + fileRelativePath;
        val file = File(path) 
        val content = file.readText().replace(Regex("${key}=.*"), "${key}=${value}");
        file.writeText(content);
    }

    fun getLogger(): Logger = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME)

    fun getLogger(name: String): Logger = LoggerFactory.getLogger(name)

    fun newLine() : String {
        return System.lineSeparator()
    }
}
