package open.tresorier.utils

import java.util.Properties
import java.io.FileInputStream

object Properties {

    fun getProperties() : Properties {
        val defaultProperties = FileReader.getPropertiesFromFile("gradle.properties")
        return getHerokuProperties(defaultProperties)
    }

    fun getHerokuProperties(default: Properties) : Properties{

        val properties = Properties(default)

        val tresorierDB = "HEROKU_POSTGRESQL_AQUA_JDBC"
        val testDB = "HEROKU_POSTGRESQL_GREEN_JDBC"
        val authDB = "HEROKU_POSTGRESQL_RED_JDBC"


        val tresorier_db_url = System.getenv(tresorierDB +"_URL") ?: default.getProperty("tresorier_db_url_dflt")
        val tresorier_db_usr = System.getenv(tresorierDB + "_USERNAME") ?: default.getProperty("tresorier_db_usr_dflt")
        val tresorier_db_pwd = System.getenv(tresorierDB + "_PASSWORD") ?: default.getProperty("tresorier_db_pwd_dflt")

        val test_db_url = System.getenv(testDB +"_URL") ?:  default.getProperty("test_db_url_dflt")
        val test_db_usr = System.getenv(testDB + "_USERNAME") ?:  default.getProperty("test_db_usr_dflt")
        val test_db_pwd = System.getenv(testDB + "_PASSWORD") ?:  default.getProperty("test_db_pwd_dflt")

        val auth_db_url = System.getenv(authDB +"_URL") ?:  default.getProperty("auth_db_url_dflt")
        val auth_db_usr = System.getenv(authDB + "_USERNAME") ?: default.getProperty("auth_db_usr_dflt")
        val auth_db_pwd = System.getenv(authDB + "_PASSWORD") ?: default.getProperty("auth_db_pwd_dflt")

        properties.setProperty("tresorier_db_url", tresorier_db_url)
        properties.setProperty("tresorier_db_usr", tresorier_db_usr)
        properties.setProperty("tresorier_db_pwd", tresorier_db_pwd)

        properties.setProperty("test_db_url", test_db_url)
        properties.setProperty("test_db_usr", test_db_usr)
        properties.setProperty("test_db_pwd", test_db_pwd)

        properties.setProperty("auth_db_url", auth_db_url)
        properties.setProperty("auth_db_usr", auth_db_usr)
        properties.setProperty("auth_db_pwd", auth_db_pwd)

        return properties
    }
}
