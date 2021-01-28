package open.tresorier.dependenciesinjection

import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import org.jooq.Configuration
import java.sql.DriverManager

import org.koin.dsl.module
import open.tresorier.utils.Properties

val dbAccess_module = module {
    single<Configuration> {DBConfiguration.configuration}
}

object DBConfiguration {

    private val properties = Properties.getProperties()

    private val userName: String = properties.getProperty("tresorier_db_usr")
    val password: String = properties.getProperty("tresorier_db_pwd")
    val url: String = properties.getProperty("tresorier_db_url")

    val connection = DriverManager.getConnection(url, userName, password)
    val configuration = DefaultConfiguration().set(connection).set(SQLDialect.POSTGRES)
}
