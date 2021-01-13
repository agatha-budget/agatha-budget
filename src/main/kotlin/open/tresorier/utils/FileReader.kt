package open.tresorier.utils

import java.util.Properties
import java.io.FileInputStream

object FileReader {

    fun getPropertiesFromFile(fileRelativePath : String) : Properties {
        val properties = Properties()
        val propertiesFile = System.getProperty("user.dir") + "/" + fileRelativePath;
        val inputStream = FileInputStream(propertiesFile)
        properties.load(inputStream)
        return properties
    }
}
