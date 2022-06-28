package open.tresorier.dependenciesinjection

import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import org.koin.dsl.module
import java.sql.DriverManager
import org.jooq.Configuration
import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import java.sql.Connection

val dbAccessTestIntegration_module = module {
    single<Configuration> {DBTestIntegrationConfiguration.configuration}
}

object DBTestIntegrationConfiguration {

    private val properties = Properties()

    private val userName: String = properties.get(INTEGRATION_DB_USR)
    private val password: String = properties.get(INTEGRATION_DB_PWD)
    private val url: String = properties.get(INTEGRATION_DB_URL)

    private val connection: Connection = DriverManager.getConnection(url, userName, password)
    val configuration: Configuration = DefaultConfiguration().set(connection).set(SQLDialect.POSTGRES)
}
