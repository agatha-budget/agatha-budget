package open.tresorier.dependenciesinjection

import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import org.koin.dsl.module
import java.sql.DriverManager
import org.jooq.Configuration
import open.tresorier.utils.Properties
import java.sql.Connection

val dbAccessTest_module = module {
    single<Configuration> {DBTestConfiguration.configuration}
}

object DBTestConfiguration {

    private val properties = Properties.getProperties()

    private val userName: String = properties.getProperty("test_db_usr")
    private val password: String = properties.getProperty("test_db_pwd")
    private val url: String = properties.getProperty("test_db_url")

    private val connection: Connection = DriverManager.getConnection(url, userName, password)
    val configuration: Configuration = DefaultConfiguration().set(connection).set(SQLDialect.H2)
}
