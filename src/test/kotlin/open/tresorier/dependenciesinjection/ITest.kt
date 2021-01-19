package open.tresorier.dependenciesinjection

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.AfterEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

interface ITest : KoinTest {

    @BeforeEach fun injectDependencies() {
        startKoin {
            printLogger()
            modules(dbAccessTest_module, app_module)
        }
    }

    @AfterEach fun stopDependencies() {
        stopKoin()
    }
}
