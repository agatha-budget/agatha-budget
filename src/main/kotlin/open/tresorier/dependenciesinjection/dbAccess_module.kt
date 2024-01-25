package open.tresorier.dependenciesinjection

import open.tresorier.utils.Properties
import open.tresorier.utils.PropertiesEnum.*
import org.jooq.Configuration
import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import org.koin.dsl.module
import java.sql.Connection
import java.sql.DriverManager

val dbAccess_module = module {
    single<Configuration> {DBConfiguration.configuration}
}

object DBConfiguration {

    private val properties = Properties()

    private val userName: String = properties.get(TRESORIER_DB_USR)
    private val password: String = properties.get(TRESORIER_DB_PWD)
    private val url: String = properties.get(TRESORIER_DB_URL)

    private val connection: Connection = DriverManager.getConnection(url, userName, password)
    val configuration: Configuration = DefaultConfiguration().set(connection).set(SQLDialect.POSTGRES)
}
