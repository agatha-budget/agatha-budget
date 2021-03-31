package open.tresorier.dependenciesinjection

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

interface ITest : KoinTest {

    @BeforeEach fun injectDependencies() {
        startKoin {
            printLogger()
            modules(dbAccessTest_module, test_dao_module, test_service_module, service_module)
        }
    }

    @AfterEach fun stopDependencies() {
        stopKoin()
    }
}
