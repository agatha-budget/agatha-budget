package open.tresorier.utils

import open.tresorier.exception.TresorierException
import open.tresorier.utils.PropertiesEnum.*
import java.io.File
import java.io.FileInputStream
import java.util.Properties as JavaProperties

class Properties () {

    val properties: JavaProperties

    // initializer block
    init {
        properties = Properties.getPropertiesFromFile("gradle.properties")
        getSystemProperties()
        getDBProperties()
    }

    fun get(name : PropertiesEnum) : String {
        return properties.getProperty(name.name) ?: throw TresorierException("could not find properties : ${name.name}")
    }

    fun getDBProperties() {
        val DB_url = this.get(DB_URL)
        val DB_usr = this.get(DB_USR)
        val DB_pwd = this.get(DB_PWD)

        properties.setProperty("DB_URL", DB_url)
        properties.setProperty("DB_USR", DB_usr)
        properties.setProperty("DB_PWD", DB_pwd)
    }

    fun getSystemProperties() {
        for (propertyEnum in PropertiesEnum.values()){
            val propertyName = propertyEnum.toString()
            System.getenv(propertyName)?.let {
                properties.setProperty(propertyName, it)
            }
        }
    }

    companion object {
        fun set(name : PropertiesEnum, value : String) {
            return Properties.setPropertyInFile("gradle.properties", name.name, value)
        }

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
    }
}
