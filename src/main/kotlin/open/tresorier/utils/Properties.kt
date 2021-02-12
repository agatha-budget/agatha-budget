package open.tresorier.utils

import java.util.Properties

object Properties {

    fun getProperties() : Properties {
        val defaultProperties = Utils.getPropertiesFromFile("gradle.properties")
        return getHerokuProperties(defaultProperties)
    }

    fun getHerokuProperties(default: Properties) : Properties{

        val properties = Properties(default)

        val tresorierDB = "HEROKU_POSTGRESQL_AQUA_JDBC"
        val testDB = "HEROKU_POSTGRESQL_GREEN_JDBC"

        val tresorier_db_url = System.getenv(tresorierDB +"_URL") ?: default.getProperty("tresorier_db_url_dflt")
        val tresorier_db_usr = System.getenv(tresorierDB + "_USERNAME") ?: default.getProperty("tresorier_db_usr_dflt")
        val tresorier_db_pwd = System.getenv(tresorierDB + "_PASSWORD") ?: default.getProperty("tresorier_db_pwd_dflt")

        val test_db_url = default.getProperty("test_db_url_dflt")
        val test_db_usr = default.getProperty("test_db_usr_dflt")
        val test_db_pwd = default.getProperty("test_db_pwd_dflt")

        properties.setProperty("tresorier_db_url", tresorier_db_url)
        properties.setProperty("tresorier_db_usr", tresorier_db_usr)
        properties.setProperty("tresorier_db_pwd", tresorier_db_pwd)

        properties.setProperty("test_db_url", test_db_url)
        properties.setProperty("test_db_usr", test_db_usr)
        properties.setProperty("test_db_pwd", test_db_pwd)

        return properties
    }
}
