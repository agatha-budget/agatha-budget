package open.tresorier.dependenciesinjection

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.AfterAll
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

interface ITest : KoinTest {

    @BeforeAll fun injectDependencies() {
        startKoin {
            printLogger()
            modules(dbAccessTest_module, app_module)
        }
    }

    @AfterAll fun stopDependencies() {
        stopKoin()
    }
}
