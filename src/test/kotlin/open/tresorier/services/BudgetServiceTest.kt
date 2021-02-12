package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class BudgetServiceTest : ITest {

    val budgetService by inject<BudgetService>()

   @Test
   fun testUpdateSomeoneElseBudget() {

   }


}
