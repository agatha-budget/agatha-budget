package open.tresorier.utils

import java.util.Properties

object Properties {
    const val aweberListId = "aweberListId"
    const val aweberAccountId = "aweberAccountId"
    const val aweberAccessToken = "aweberAccessToken"

    fun getProperties() : Properties {
        val defaultProperties = Utils.getPropertiesFromFile("gradle.properties")
        return getHerokuProperties(defaultProperties)
    }

    fun getHerokuProperties(default: Properties) : Properties{

        val properties = Properties(default)

        val tresorierDB = default.getProperty("herokuTresorierDB")
        val integrationDB = default.getProperty("herokuIntegrationDB")

        val tresorier_db_url = System.getenv(tresorierDB +"_URL") ?: default.getProperty("tresorier_db_url_dflt")
        val tresorier_db_usr = System.getenv(tresorierDB + "_USERNAME") ?: default.getProperty("tresorier_db_usr_dflt")
        val tresorier_db_pwd = System.getenv(tresorierDB + "_PASSWORD") ?: default.getProperty("tresorier_db_pwd_dflt")

        val test_db_url = default.getProperty("test_db_url")
        val test_db_usr = default.getProperty("test_db_usr")
        val test_db_pwd = default.getProperty("test_db_pwd")

        val integration_db_url = System.getenv(integrationDB +"_URL") ?: default.getProperty("integration_db_url_dflt")
        val integration_db_usr = System.getenv(integrationDB + "_USERNAME") ?: default.getProperty("integration_db_usr_dflt")
        val integration_db_pwd = System.getenv(integrationDB + "_PASSWORD") ?: default.getProperty("integration_db_pwd_dflt")

        properties.setProperty("tresorier_db_url", tresorier_db_url)
        properties.setProperty("tresorier_db_usr", tresorier_db_usr)
        properties.setProperty("tresorier_db_pwd", tresorier_db_pwd)

        properties.setProperty("test_db_url", test_db_url)
        properties.setProperty("test_db_usr", test_db_usr)
        properties.setProperty("test_db_pwd", test_db_pwd)

        properties.setProperty("integration_db_url", integration_db_url)
        properties.setProperty("integration_db_usr", integration_db_usr)
        properties.setProperty("integration_db_pwd", integration_db_pwd)

        return properties
    }
}
