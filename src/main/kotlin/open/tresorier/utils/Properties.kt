package open.tresorier.utils

import java.util.Properties as JavaProperties
import open.tresorier.utils.PropertiesEnum.*

class Properties () {

    val properties: JavaProperties

    // initializer block
    init {
        properties = Utils.getPropertiesFromFile("gradle.properties")
        getSystemProperties()
        getDBProperties()
        System.out.println("ewfuefuoeofiewfo")
        System.out.println(this.get(AWEBER_LIST_ID))
        System.out.println(this.get(TRESORIER_DB_URL_DFLT))
        System.out.println("ewfuefuoeofiewfo")

    }

    fun get(name : PropertiesEnum) : String {
        return properties.getProperty(name.name)
    }

    fun getDBProperties() {
        val tresorierDB = this.get(TRESORIER_DB_ID)
        val integrationDB = this.get(INTEGRATION_DB_ID)

        val tresorier_db_url = System.getenv(tresorierDB +"_URL") ?: this.get(TRESORIER_DB_URL_DFLT)
        val tresorier_db_usr = System.getenv(tresorierDB + "_USERNAME") ?: this.get(TRESORIER_DB_USR_DFLT)
        val tresorier_db_pwd = System.getenv(tresorierDB + "_PASSWORD") ?: this.get(TRESORIER_DB_PWD_DFLT)

        val integration_db_url = System.getenv(integrationDB +"_URL") ?: this.get(INTEGRATION_DB_URL_DFLT)
        val integration_db_usr = System.getenv(integrationDB + "_USERNAME") ?: this.get(INTEGRATION_DB_USR_DFLT)
        val integration_db_pwd = System.getenv(integrationDB + "_PASSWORD") ?: this.get(INTEGRATION_DB_PWD_DFLT)

        properties.setProperty("TRESORIER_DB_URL", tresorier_db_url)
        properties.setProperty("TRESORIER_DB_USR", tresorier_db_usr)
        properties.setProperty("TRESORIER_DB_PWD", tresorier_db_pwd)

        properties.setProperty("INTEGRATION_DB_URL", integration_db_url)
        properties.setProperty("INTEGRATION_DB_USR", integration_db_usr)
        properties.setProperty("INTEGRATION_DB_PWD", integration_db_pwd)
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
            return Utils.setPropertyInFile("gradle.properties", name.name, value)
        }
    }
}
