package open.tresorier.services

import open.tresorier.dependenciesinjection.ITest
import open.tresorier.model.Person
import open.tresorier.model.enum.ProfileEnum
import open.tresorier.utils.Time
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.koin.core.component.inject

class PersonServiceTest : ITest {

    private val personService by inject<PersonService>()

    @Test
    fun testValidlogin() {
        personService.createPerson("Olympe de Gouge", "DeclarationDroit", "olympe@revolution.fr", ProfileEnum.PROFILE_USER)
        val olympe = personService.login("olympe@revolution.fr", "DeclarationDroit")
        assertNotNull(olympe)
    }

    @Test
    fun testFailFirstTwoLogin() {
        val leodile: Person =
            personService.createPerson("Léodile Champseix", "Egalité!", "leodile@champseix.eu", ProfileEnum.PROFILE_USER)
        val before = Time.now()
        // First 3 tries are free (after the third you have to wait)
        personService.login("leodile@champseix.eu", "SecondEmpire")
        val personShouldBeNull: Person? = personService.login("leodile@champseix.eu", "Liberté!")
        assertNull(personShouldBeNull)
        val leodileById: Person = personService.getById(leodile.id)
        assertEquals(2, leodileById.loginAttemptCount)
        assertTrue(leodileById.unlockingDate >= before)
        assertTrue(leodileById.unlockingDate <= Time.now())
    }

    @Test
    fun testFailFourLogin() {
        val simone: Person = personService.createPerson(
            "Simone Veil",
            "Contraception!1974",
            "simone@planning-famillial.fr",
            ProfileEnum.PROFILE_USER
        )
        val before = Time.now()
        personService.login("simone@planning-famillial.fr", "1949")
        personService.login("simone@planning-famillial.fr", "1967")
        personService.login("simone@planning-famillial.fr", "1968")
        personService.login("simone@planning-famillial.fr", "1972")

        val simoneById = personService.getById(simone.id)
        assertEquals(4, simoneById.loginAttemptCount)

        // check unlocking date
        val delayedBy = 3 * Time.getDuration(minutes = 5)
        assertTrue((before + delayedBy) <= simoneById.unlockingDate)
        assertTrue((Time.now() + delayedBy) >= simoneById.unlockingDate)
    }

    @Test
    fun testFailLoginRighPasswordButLockedAccount() {
        val julie: Person =
            personService.createPerson("Julie Daubié", "Bacheliere!1861", "julie@diplomee.com", ProfileEnum.PROFILE_USER)
        val before = Time.now()
        personService.login("julie@diplomee.com", "Certificat")
        personService.login("julie@diplomee.com", "Brevet")
        personService.login("julie@diplomee.com", "CAP")
        personService.login("julie@diplomee.com", "BEP")

        val personShouldBeNull: Person? = personService.login("julie@diplomee.com", "Bacheliere!1861")
        assertNull(personShouldBeNull)
        val julieById: Person = personService.getById(julie.id)
        assertEquals(0, julieById.loginAttemptCount)

        // check unlocking date
        val delayedBy = 3 * Time.getDuration(minutes = 5)
        assertTrue((before + delayedBy) <= julieById.unlockingDate)
        assertTrue((Time.now() + delayedBy) >= julieById.unlockingDate)
    }

    @Test
    fun testWorkingLoginAfterFail() {
        val louise: Person = personService.createPerson("Louise Weiss", "Vote!1944", "louise@suffragette.fr", ProfileEnum.PROFILE_USER)
        val now = Time.now()
        personService.login("louise@suffragette.fr", "1919")
        personService.login("louise@suffragette.fr", "1935")
        personService.login("louise@suffragette.fr", "1936")
        personService.login("louise@suffragette.fr", "1938")

        // Once time is passed
        val louiseById: Person = personService.getById(louise.id)
        louiseById.unlockingDate = now
        personService.update(louise)

        val person: Person? = personService.login("louise@suffragette.fr", "Vote!1944")
        assertNotNull(person)
        assertEquals(0, person?.loginAttemptCount)
    }

    // will break in 2777, increase date if needed ;)
    @Test
    fun testAddIncrementalDelay() {
        val dateIn2777: Long = 25482668827000
        val delayed = personService.addIncrementalDelay(dateIn2777, 4)
        // add (param-2)*5 min
        val dateIn2777And10Min: Long = 25482669427000
        assertEquals(dateIn2777And10Min, delayed)
    }
}
