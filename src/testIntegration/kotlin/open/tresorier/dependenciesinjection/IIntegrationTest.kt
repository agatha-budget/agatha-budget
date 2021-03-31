package open.tresorier.dependenciesinjection

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

interface IIntegrationTest : KoinTest {

    @BeforeEach fun injectDependencies() {
        startKoin {
            printLogger()
            modules(dbAccessTestIntegration_module, dao_module)
        }
    }

    @AfterEach fun stopDependencies() {
        stopKoin()
    }
}
