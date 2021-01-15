package open.tresorier.dependenciesinjection

import open.tresorier.dao.IPersonDao
import open.tresorier.dao.jooq.JooqPersonDao
import open.tresorier.services.PersonService
import org.jooq.SQLDialect
import org.jooq.impl.DefaultConfiguration
import org.koin.dsl.module
import java.sql.DriverManager
import org.jooq.Configuration
import open.tresorier.generated.jooq.tables.daos.PersonDao as GeneratedPersonDao
import open.tresorier.utils.Properties

val dbAccessTest_module = module {
    single<Configuration> {DBTestConfiguration.configuration}
}

object DBTestConfiguration {

    val properties = Properties.getProperties()

    val userName = properties.getProperty("test_db_usr")
    val password = properties.getProperty("test_db_pwd")
    val url = properties.getProperty("test_db_url")

    val connection = DriverManager.getConnection(url, userName, password)
    val configuration = DefaultConfiguration().set(connection).set(SQLDialect.POSTGRES)
}
