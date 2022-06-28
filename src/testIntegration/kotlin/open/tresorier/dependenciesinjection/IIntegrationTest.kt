package open.tresorier.dependenciesinjection

import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.startKoin

interface IIntegrationTest : ITest {

    @BeforeEach override fun injectDependencies() {
        startKoin {
            printLogger()
            modules(dbAccessTestIntegration_module, adapter_module)
        }
    }
}
